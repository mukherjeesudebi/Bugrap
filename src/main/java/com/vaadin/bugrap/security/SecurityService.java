package com.vaadin.bugrap.security;

import org.vaadin.bugrap.domain.entities.Reporter;


public interface SecurityService {
	 public Reporter getAuthenticatedUser();
}
