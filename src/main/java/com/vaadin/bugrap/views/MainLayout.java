package com.vaadin.bugrap.views;

import javax.annotation.security.PermitAll;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.CommentDao;
import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.AuthenticatedUser;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PermitAll
@PageTitle("bugrap")
@Route(value = "bugrap")
@RouteAlias(value = "")
public class MainLayout extends SplitLayout {

    private ProjectDao projectDao;
    private ReporterDao reporterDao;
    private CommentDao commentDao;

    private ProjectVersionService projectVersionService;
    private ReportService reportService;

    private HeaderLayout headerLayout;
    private BodyLayout bodyLayout;

    private Project selectedProject;
    private VerticalLayout verticalLayout;
    private ReportDetailsLayout reportDetailsLayout;
    private AuthenticatedUser authenticatedUserImpl;

    public MainLayout(ProjectDao projectDao,
            ProjectVersionService projectVersionService,
            ReportService reportService, ReporterDao reporterDao,
            AuthenticatedUser authenticatedUserImpl,CommentDao commentDao) {
        this.projectDao = projectDao;
        this.projectVersionService = projectVersionService;
        this.reportService = reportService;
        this.reporterDao = reporterDao;
        this.authenticatedUserImpl = authenticatedUserImpl;
        this.commentDao = commentDao;
        verticalLayout = new VerticalLayout();
        verticalLayout.addClassName(LumoUtility.Padding.NONE);
        reportDetailsLayout = new ReportDetailsLayout(this.reporterDao,
                this.projectVersionService, this.reportService,this.commentDao,this.authenticatedUserImpl);

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
        headerLayout = new HeaderLayout(projectDao, projectVersionService,
               authenticatedUserImpl);
        verticalLayout.add(headerLayout);
        selectedProject = headerLayout.getSelectedProject();
        Button testButton = new Button("testBitton");
        testButton.setId("testButton");
        //testButton.getStyle().set("visibility", "hidden");
        testButton.getStyle().set("display", "none");
        testButton.addClickListener(event -> {
            UI.getCurrent().navigate(DistributionBarLayout.class);
        });
        headerLayout.add(testButton);
    }

    public void addBody() {
        bodyLayout = new BodyLayout(projectVersionService, reportService,
                selectedProject);
        verticalLayout.add(bodyLayout);
        headerLayout.setBodyLayout(bodyLayout);
        headerLayout.setReportDetailsLayout(reportDetailsLayout);
    }

}
