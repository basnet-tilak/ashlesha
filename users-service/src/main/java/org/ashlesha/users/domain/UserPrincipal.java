package org.ashlesha.users.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.ashlesha.common.domain.authorization.Role;
import org.ashlesha.common.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserPrincipal(User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        return java.util.stream.Stream.concat(
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())),
                roles.stream().flatMap(role -> role.getPermissions().stream())
                        .map(permission -> new SimpleGrantedAuthority("SCOPE_" + permission.getName()))
        ).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getStatus().equals(org.ashlesha.common.domain.user.UserStatus.DEACTIVATED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountLockedUntil() == null || user.getAccountLockedUntil().isBefore(java.time.LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == org.ashlesha.common.domain.user.UserStatus.ACTIVE;
    }
}
