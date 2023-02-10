package com.vaadin.bugrap.views;

import javax.annotation.security.PermitAll;

import com.vaadin.bugrap.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PermitAll
@Route(value="login")
public class LoginLayout extends LoginOverlay implements BeforeEnterObserver {
    /*
     * private LoginForm loginForm = new LoginForm(); private ReporterDao
     * reporterDao;
     * 
     * public LoginLayout(ReporterDao reporterDao) { this.reporterDao =
     * reporterDao; addClassName("login-layout"); setSizeFull();
     * setAlignItems(Alignment.CENTER);
     * setJustifyContentMode(JustifyContentMode.CENTER); add(new H1("BUGRAP"),
     * loginForm); // loginForm.addLoginListener(event-> //
     * dologin(event.getUsername(),event.getPassword()));
     * loginForm.addLoginListener(event -> { if (event.getUsername() != null &&
     * event.getPassword() != null) { dologin(event.getUsername(),
     * event.getPassword()); } }); }
     * 
     * public void dologin(String username, String password) { Reporter user =
     * reporterDao.findReporterByName(username,password); if (user != null) {
     * if(user.verifyPassword(password)) { Authentication auth = new
     * PreAuthenticatedAuthenticationToken(user, null);
     * auth.setAuthenticated(true);
     * SecurityContextHolder.getContext().setAuthentication(auth);
     * UI.getCurrent().navigate(MainLayout.class);
     * //UI.getCurrent().navigate(DistributionBarLayout.class); } } }
     * 
     */

    private final AuthenticatedUser authenticatedUser;

    public LoginLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction(RouteUtil.getRoutePath(
                VaadinService.getCurrent().getContext(), getClass()));

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Bugrap Project");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters()
                .containsKey("error"));
    }

}
