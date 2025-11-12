package org.ashlesha.users.dto;

import java.time.LocalDateTime;

public record RegistrationResponse(Long userId, String username, String email, LocalDateTime registeredAt) {
}
