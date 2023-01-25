package com.vaadin.bugrap.views;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ProjectVersionDao;
import com.vaadin.bugrap.dao.ReportDao;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private ProjectDao projectDao;
	private ProjectVersionDao projectVersionDao;
	private ReportDao reportDao;
	
	private HeaderLayout headerLayout;
	private BodyLayout bodyLayout;
	
	private Project selectedProject;

	public MainLayout(ProjectDao projectDao, ProjectVersionDao projectVersionDao, ReportDao reportDao) {
		this.projectDao = projectDao;
		this.projectVersionDao = projectVersionDao;
		this.reportDao = reportDao;

		addHeader();
		addBody();
	}

	public void addHeader() {
		headerLayout = new HeaderLayout(projectDao);
		add(headerLayout);
		selectedProject = headerLayout.getSelectedProject();
	}

	public void addBody() {
		bodyLayout = new BodyLayout(projectVersionDao, reportDao,selectedProject);
		add(bodyLayout);
		headerLayout.setBodyLayout(bodyLayout);
	}

}
