package com.vaadin.bugrap.security;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Reporter;

@Component
public class MockSecurityServiceImpl implements SecurityService {
    public Reporter getAuthenticatedUser() {
        Reporter admin = new Reporter();
        admin.setName("admin");
        admin.setEmail("admin@bugrap.com");
        admin.setConsistencyVersion(0);
        admin.setAdmin(false);
        admin.setId(1);
        return admin;
    }
}
