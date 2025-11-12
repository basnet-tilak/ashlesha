package org.ashlesha.common.domain.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ashlesha.common.domain.authorization.Role;
import org.ashlesha.common.domain.security.MFASetup;
import org.ashlesha.common.domain.security.APIToken;
import org.ashlesha.common.domain.security.PasswordHistory;
import org.ashlesha.common.domain.security.LoginHistory;
import org.ashlesha.common.domain.kyc.KYCVerification;
import org.ashlesha.common.domain.audit.AuditLog;
import org.ashlesha.common.domain.user.consent.ConsentRecord;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max = 64)
    private String username;

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @NotBlank
    @Size(max = 256)
    private String passwordHash;

    @Size(max = 256)
    private String passwordSalt;

    private LocalDateTime lastPasswordChange;

    private int failedLoginAttempts;

    private LocalDateTime accountLockedUntil;

    @Size(max = 256)
    private String lockReason;

    private Boolean termsAccepted = Boolean.FALSE;

    private LocalDateTime termsAcceptedAt;

    @Size(max = 16)
    private String preferredLanguage;

    @Size(max = 64)
    private String timezone;

    private LocalDateTime lastActivityAt;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING_ACTIVATION;

    private LocalDateTime statusChangedAt;

    @Size(max = 256)
    private String statusChangeReason;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoginHistory> loginHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordHistory> passwordHistory = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MFASetup> mfaSetups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<APIToken> apiTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KYCVerification> verifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditLog> auditLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsentRecord> consentRecords = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public void resetPassword(String passwordHash, String passwordSalt) {
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.lastPasswordChange = LocalDateTime.now();
    }

    public void lockAccount(String reason, LocalDateTime until) {
        this.status = UserStatus.LOCKED;
        this.accountLockedUntil = until;
        this.lockReason = reason;
        this.statusChangedAt = LocalDateTime.now();
    }

    public void unlockAccount() {
        this.status = UserStatus.ACTIVE;
        this.accountLockedUntil = null;
        this.lockReason = null;
        this.failedLoginAttempts = 0;
        this.statusChangedAt = LocalDateTime.now();
    }

    public void enableMFA(MFASetup setup) {
        setup.setUser(this);
        setup.setEnabled(true);
        setup.setEnabledAt(LocalDateTime.now());
        this.mfaSetups.add(setup);
    }

    public void disableMFA() {
        if (mfaSetups != null) {
            mfaSetups.forEach(mfa -> mfa.setEnabled(false));
        }
    }
}
