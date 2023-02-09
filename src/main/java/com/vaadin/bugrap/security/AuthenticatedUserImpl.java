package com.vaadin.bugrap.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Reporter;
import org.vaadin.bugrap.domain.spring.ReporterRepository;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

@Component
public class AuthenticatedUserImpl implements AuthenticatedUser {

    private final ReporterRepository reporterRepository;

    @Autowired
    public AuthenticatedUserImpl(ReporterRepository reporterRepository) {
        this.reporterRepository = reporterRepository;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication()).filter(
                authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<Reporter> get() {
        if (!getAuthentication().isEmpty()) {
            Authentication auth = getAuthentication().get();
            auth.getName();
        }
        return getAuthentication().map(authentication -> reporterRepository
                .getByNameOrEmail(authentication.getName(), null));
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfig.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

}
