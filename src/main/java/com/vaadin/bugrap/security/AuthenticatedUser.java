package com.vaadin.bugrap.security;

import java.util.Optional;

import org.vaadin.bugrap.domain.entities.Reporter;

public interface AuthenticatedUser {

    public Optional<Reporter> get();
    
    public void logout();
}
