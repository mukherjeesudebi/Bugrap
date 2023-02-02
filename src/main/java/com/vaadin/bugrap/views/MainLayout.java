package com.vaadin.bugrap.views;

import javax.annotation.security.PermitAll;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.SecurityService;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PermitAll
@Route("bugrap")
public class MainLayout extends SplitLayout {

	private ProjectDao projectDao;
	private ReporterDao reporterDao;
	
	private ProjectVersionService projectVersionService;
	private ReportService reportService;
	
	private HeaderLayout headerLayout;
	private BodyLayout bodyLayout;
	
	private Project selectedProject;
	private VerticalLayout verticalLayout;
	private ReportDetailsLayout reportDetailsLayout;
	private SecurityService securityService;
	public MainLayout(ProjectDao projectDao, ProjectVersionService projectVersionService, ReportService reportService,ReporterDao reporterDao,SecurityService securityService) {
		this.projectDao = projectDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		this.reporterDao = reporterDao;
		this.securityService = securityService;
		
		verticalLayout = new VerticalLayout();
		verticalLayout.addClassName(LumoUtility.Padding.NONE);
		reportDetailsLayout = new ReportDetailsLayout(this.reporterDao,this.projectVersionService,this.reportService);
		
		addHeader();
		addBody();	
		
		addToPrimary(verticalLayout);
		addToSecondary(reportDetailsLayout);
		setOrientation(SplitLayout.Orientation.VERTICAL);
		setHeightFull();
		setSplitterPosition(60);
		
		bodyLayout.setReportDetailsLayout(reportDetailsLayout);
		reportDetailsLayout.setGrid(bodyLayout.getGrid());
		reportDetailsLayout.connectGrid();
		reportDetailsLayout.setSelectedProject(this.selectedProject);
		reportDetailsLayout.createReportDetails();
		addThemeName("bugrap-main");
	}

	public void addHeader() {
		headerLayout = new HeaderLayout(projectDao,projectVersionService,securityService);
		verticalLayout.add(headerLayout);
		selectedProject = headerLayout.getSelectedProject();
	}

	public void addBody() {		
		bodyLayout = new BodyLayout(projectVersionService, reportService,selectedProject);
		verticalLayout.add(bodyLayout);
		headerLayout.setBodyLayout(bodyLayout);	
		headerLayout.setReportDetailsLayout(reportDetailsLayout);
	}

}
