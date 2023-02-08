package com.vaadin.bugrap.views;

import javax.annotation.security.PermitAll;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@PermitAll
@Route("login")
public class LoginLayout extends VerticalLayout {
	private LoginForm loginForm = new LoginForm();
	private ReporterDao reporterDao;

	public LoginLayout(ReporterDao reporterDao) {
		this.reporterDao = reporterDao;
		addClassName("login-layout");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		add(new H1("BUGRAP"), loginForm);
		// loginForm.addLoginListener(event->
		// dologin(event.getUsername(),event.getPassword()));
		loginForm.addLoginListener(event -> {
			if (event.getUsername() != null && event.getPassword() != null) {
				dologin(event.getUsername(), event.getPassword());
			}
		});
	}

	public void dologin(String username, String password) {
		Reporter user = reporterDao.findReporterByName(username,password);
		if (user != null) {
			if(user.verifyPassword(password)) {
			Authentication auth = new PreAuthenticatedAuthenticationToken(user, null);
			auth.setAuthenticated(true);
			SecurityContextHolder.getContext().setAuthentication(auth);
			UI.getCurrent().navigate(MainLayout.class);
			//UI.getCurrent().navigate(DistributionBarLayout.class);
			}
		}
	}

}
