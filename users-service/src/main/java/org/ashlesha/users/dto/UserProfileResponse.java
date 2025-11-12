package org.ashlesha.users.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserProfileResponse(
        Long userId,
        String username,
        String email,
        String fullName,
        Set<String> roles,
        LocalDateTime lastActivityAt,
        LocalDateTime lastPasswordChange
) {
}
