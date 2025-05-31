package org.thivernale.orderservice.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public String getCurrentUsername() {
        // TODO get from credentials
        return "username";
    }
}
