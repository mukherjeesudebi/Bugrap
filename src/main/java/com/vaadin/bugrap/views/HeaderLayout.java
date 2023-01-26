package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class HeaderLayout extends HorizontalLayout {
	
	private ProjectDao projectDao;
	private Select<Project> projectSelect;
	private Project selectedProject;
	private ProjectVersionService projectVersionService;
	
	private BodyLayout bodyLayout;
	private ReportDetailsLayout reportDetailsLayout;

	public HeaderLayout(ProjectDao projectDao,ProjectVersionService projectVersionService) {
		this.projectDao = projectDao;
		this.projectVersionService = projectVersionService;
		createHeader();
	}
	
	public void createHeader(){
		projectSelect = new Select<Project>();
		List<Project> allProjectLists = projectDao.getAllProjectsList();
		this.selectedProject = allProjectLists.get(0);
		projectSelect.setItems(allProjectLists);
		projectSelect.setItemLabelGenerator(Project::getName);
		projectSelect.setValue(this.selectedProject);

		projectSelect.addValueChangeListener(event -> {
			this.getBodyLayout().loadProjectVersions(event.getValue());
			this.getBodyLayout().loadReports(event.getValue());
			this.getReportDetailsLayout().getProjectVersionSelect().setItems(this.projectVersionService.getAllProjectVersions(this.selectedProject));
		});
	
		add(projectSelect);

		HorizontalLayout headerHorizontalLayoutRight = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		Div userName = new Div();
		userName.setText("Marc Manager");
		Icon powerOffIcon = new Icon(VaadinIcon.POWER_OFF);

		/*
		 * TextField searchField = new TextField();
		 * searchField.setSuffixComponent(VaadinIcon.SEARCH.create());
		 */

		headerHorizontalLayoutRight.add(userIcon, userName, powerOffIcon);
		headerHorizontalLayoutRight.addClassName(LumoUtility.TextColor.PRIMARY);
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
}
