package com.vaadin.bugrap.views;

import java.util.List;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.security.SecurityService;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.themes.LumoLightTheme;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class HeaderLayout extends HorizontalLayout {

	private ProjectDao projectDao;
	private Select<Project> projectSelect;
	private Project selectedProject;
	private ProjectVersionService projectVersionService;

	private BodyLayout bodyLayout;
	private ReportDetailsLayout reportDetailsLayout;
	private SecurityService securityService;

	public HeaderLayout(ProjectDao projectDao, ProjectVersionService projectVersionService,
			SecurityService securityService) {
		this.projectDao = projectDao;
		this.projectVersionService = projectVersionService;
		this.securityService = securityService;
		createHeader();
	}

	public void createHeader() {
		projectSelect = new Select<Project>();
		projectSelect.addThemeName("bugrap-select");
		List<Project> allProjectLists = projectDao.getAllProjectsList();
		this.selectedProject = allProjectLists.get(0);
		projectSelect.setItems(allProjectLists);
		projectSelect.setItemLabelGenerator(Project::getName);
		projectSelect.setValue(this.selectedProject);

		projectSelect.addValueChangeListener(event -> {
			this.getBodyLayout().setSelectedProject(event.getValue());
			this.getBodyLayout().loadProjectVersions(event.getValue());
			this.getBodyLayout().loadReports(event.getValue());
			this.reportDetailsLayout.getReportprojectVersionSelect()
					.setItems(this.projectVersionService.getAllProjectVersions(event.getValue()));
			// this.getReportDetailsLayout().getReportprojectVersionSelect().setItems(this.projectVersionService.getAllProjectVersions(this.selectedProject));
		});

		add(projectSelect);
		addClassName(LumoUtility.Padding.Left.MEDIUM);
		addClassName(LumoUtility.Padding.Right.MEDIUM);

		HorizontalLayout headerHorizontalLayoutRight = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		userIcon.addClassName("bugrapUserIcon");
		Div userName = new Div();
		userName.setText(securityService.getAuthenticatedUser().getName());
		Button powerOffIcon = new Button("", new Icon(VaadinIcon.POWER_OFF));
		powerOffIcon.addThemeName("bugrap-button-link");
		powerOffIcon.addClickListener(event -> {
			this.logout();
		});

		/*
		 * TextField searchField = new TextField();
		 * searchField.setSuffixComponent(VaadinIcon.SEARCH.create());
		 */

		headerHorizontalLayoutRight.add(userIcon, userName, powerOffIcon);
		headerHorizontalLayoutRight.setAlignItems(Alignment.CENTER);
		headerHorizontalLayoutRight.getStyle().set("color", "#414fbc");
		add(headerHorizontalLayoutRight);

		setWidthFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.BETWEEN);
//		headerHorizontalLayout.getStyle().set("box-shadow", "0 4px 7px -2px gray");
		addClassNames(LumoUtility.BoxShadow.SMALL);
		// LumoUtility.Padding.MEDIUM
		setHeightFull();
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public BodyLayout getBodyLayout() {
		return bodyLayout;
	}

	public void setBodyLayout(BodyLayout bodyLayout) {
		this.bodyLayout = bodyLayout;
	}

	public ReportDetailsLayout getReportDetailsLayout() {
		return reportDetailsLayout;
	}

	public void setReportDetailsLayout(ReportDetailsLayout reportDetailsLayout) {
		this.reportDetailsLayout = reportDetailsLayout;
	}

	public void logout() {
		// getUI().get().getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
	}
}
