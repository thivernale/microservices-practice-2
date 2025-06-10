/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thivernale.orderservice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class WithMockOauth2SecurityContextFactory implements WithSecurityContextFactory<WithMockJwtAuth> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuth withUser) {
        String username = StringUtils.hasLength(withUser.username()) ? withUser.username() : withUser.value();
        Assert.notNull(username, () -> withUser + " cannot have null username on both username and value properties");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : withUser.roles()) {
            Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        Map<String, Object> claims = Map.of(
            "sub", withUser.id(),
            "preferred_username", username,
            "realm_access", grantedAuthorities
        );
        Map<String, Object> headers = Map.of("mock-header", "mock-value");
        Jwt jwt = new Jwt("mock-jwt-token", Instant.now(), Instant.now()
            .plus(300, ChronoUnit.SECONDS), headers, claims);
        Authentication authentication = new JwtAuthenticationToken(jwt, grantedAuthorities, username);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
