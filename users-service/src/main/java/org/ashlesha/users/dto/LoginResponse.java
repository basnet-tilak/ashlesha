package org.ashlesha.users.dto;

import java.time.Instant;
import java.util.Set;

public record LoginResponse(String token, Instant expiresAt, Set<String> roles) {
}
