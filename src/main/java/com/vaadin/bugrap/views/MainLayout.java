package com.vaadin.bugrap.views;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ProjectVersionDao;
import com.vaadin.bugrap.dao.ReportDao;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends SplitLayout {

	private ProjectDao projectDao;
	private ProjectVersionDao projectVersionDao;
	private ReportDao reportDao;
	
	private HeaderLayout headerLayout;
	private BodyLayout bodyLayout;
	
	private Project selectedProject;
	private VerticalLayout verticalLayout;
	private ReportDetailsLayout reportDetailsLayout;

	public MainLayout(ProjectDao projectDao, ProjectVersionDao projectVersionDao, ReportDao reportDao) {
		this.projectDao = projectDao;
		this.projectVersionDao = projectVersionDao;
		this.reportDao = reportDao;
		
		verticalLayout = new VerticalLayout();
		reportDetailsLayout = new ReportDetailsLayout();
		
		addHeader();
		addBody();	
		
		addToPrimary(verticalLayout);
		addToSecondary(reportDetailsLayout);
		setOrientation(SplitLayout.Orientation.VERTICAL);
		
		bodyLayout.setReportDetailsLayout(reportDetailsLayout);
		reportDetailsLayout.setGrid(bodyLayout.getGrid());
		reportDetailsLayout.connectGrid();
	}

	public void addHeader() {
		headerLayout = new HeaderLayout(projectDao);
		verticalLayout.add(headerLayout);
		selectedProject = headerLayout.getSelectedProject();
	}

	public void addBody() {		
		bodyLayout = new BodyLayout(projectVersionDao, reportDao,selectedProject);
		verticalLayout.add(bodyLayout);
		headerLayout.setBodyLayout(bodyLayout);		
	}

}
