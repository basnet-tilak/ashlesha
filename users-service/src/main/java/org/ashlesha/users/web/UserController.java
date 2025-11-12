package org.ashlesha.users.web;

import lombok.RequiredArgsConstructor;
import org.ashlesha.users.dto.UserProfileResponse;
import org.ashlesha.users.service.IdentityService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IdentityService identityService;

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        return identityService.currentUserProfile(username);
    }
}
