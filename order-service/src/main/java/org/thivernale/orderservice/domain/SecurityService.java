package org.thivernale.orderservice.domain;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public String getCurrentUsername() {
        if (SecurityContextHolder.getContext()
            .getAuthentication() instanceof JwtAuthenticationToken token &&
            token.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }
        return "unknown";
    }
}
