package org.ashlesha.users.service;

import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.ashlesha.common.domain.audit.AuditEventType;
import org.ashlesha.common.domain.audit.AuditLog;
import org.ashlesha.common.domain.authorization.Role;
import org.ashlesha.common.domain.security.LoginHistory;
import org.ashlesha.common.domain.security.PasswordHistory;
import org.ashlesha.common.domain.user.User;
import org.ashlesha.common.domain.user.UserProfile;
import org.ashlesha.common.domain.user.UserStatus;
import org.ashlesha.users.dto.LoginRequest;
import org.ashlesha.users.dto.LoginResponse;
import org.ashlesha.users.dto.RegistrationRequest;
import org.ashlesha.users.dto.RegistrationResponse;
import org.ashlesha.users.dto.UserProfileResponse;
import org.ashlesha.users.repository.RoleRepository;
import org.ashlesha.users.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);
    private static final String DEFAULT_ROLE = "KUBERWALLET_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        if (!request.termsAccepted()) {
            throw new IllegalArgumentException("Terms and consent must be accepted to register.");
        }
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new IllegalArgumentException("Username already registered.");
        }
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setLastPasswordChange(LocalDateTime.now());
        user.setTermsAccepted(true);
        user.setTermsAcceptedAt(LocalDateTime.now());
        user.setStatus(UserStatus.ACTIVE);
        user.setStatusChangedAt(LocalDateTime.now());

        UserProfile profile = new UserProfile();
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        user.setProfile(profile);

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(DEFAULT_ROLE);
                    role.setDescription("Default wallet collaborator role");
                    role.setSystemRole(true);
                    return roleRepository.save(role);
                });
        user.addRole(defaultRole);

        PasswordHistory history = new PasswordHistory();
        history.setPasswordHash(user.getPasswordHash());
        history.setPasswordSalt(null);
        history.setChangedBy("SYSTEM");
        history.setUser(user);
        user.getPasswordHistory().add(history);

        AuditLog auditLog = new AuditLog();
        auditLog.setEventType(AuditEventType.PROFILE_UPDATE);
        auditLog.setDescription("User registration completed");
        auditLog.setSuccess(true);
        auditLog.setOccurredAt(LocalDateTime.now());
        auditLog.setUser(user);
        user.getAuditLogs().add(auditLog);

        User saved = userRepository.save(user);
        return new RegistrationResponse(saved.getUserId(), saved.getUsername(), saved.getEmail(), saved.getStatusChangedAt());
    }

    @Transactional
    public LoginResponse authenticate(LoginRequest request, String ipAddress, String userAgent) {
        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.username(), request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new LockedException("Account locked until " + user.getAccountLockedUntil());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            if (user.getStatus() == UserStatus.LOCKED) {
                user.unlockAccount();
            } else {
                user.setFailedLoginAttempts(0);
                user.setAccountLockedUntil(null);
            }
            user.setLastActivityAt(LocalDateTime.now());

            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setUser(user);
            loginHistory.setSuccess(true);
            loginHistory.setIpAddress(ipAddress);
            loginHistory.setUserAgent(userAgent);
            user.getLoginHistory().add(loginHistory);

            String token = jwtTokenService.generateToken(user);
            userRepository.save(user);
            java.time.Instant issuedAt = java.time.Instant.now();
            return new LoginResponse(token,
                    issuedAt.plus(jwtTokenService.accessTokenTtl()),
                    user.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()));
        } catch (BadCredentialsException ex) {
            handleFailedLogin(user, ipAddress, userAgent);
            throw ex;
        }
    }

    private void handleFailedLogin(User user, String ipAddress, String userAgent) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.lockAccount("Maximum failed login attempts reached", LocalDateTime.now().plus(LOCK_DURATION));
        }
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setSuccess(false);
        loginHistory.setFailureReason("Invalid credentials");
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setUserAgent(userAgent);
        user.getLoginHistory().add(loginHistory);
        userRepository.save(user);
    }

    @Transactional
    public UserProfileResponse currentUserProfile(String username) {
        User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet());
        String fullName = user.getProfile() != null ? user.getProfile().getFullName() : null;
        return new UserProfileResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                fullName,
                roles,
                user.getLastActivityAt(),
                user.getLastPasswordChange());
    }
}
