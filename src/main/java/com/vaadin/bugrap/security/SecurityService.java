package com.vaadin.bugrap.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Reporter;

@Component
public class SecurityService {
	 public Reporter getAuthenticatedUser() {
	        SecurityContext context = SecurityContextHolder.getContext();
	        Object principal = context.getAuthentication().getPrincipal();
	        if (principal instanceof Reporter) {
	            return (Reporter) principal;
	        }
	        return null;
	    }
}
