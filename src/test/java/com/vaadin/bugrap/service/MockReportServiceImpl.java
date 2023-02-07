package com.vaadin.bugrap.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

public class MockReportServiceImpl implements ReportService{

    @Override
    public List<Report> getAllProjectReports(Project project) {
        List<Report> projectReportsList = new ArrayList<Report>();
        Project mockProject = new Project();
        Reporter reporter = new Reporter();
        reporter.setName("manager");
        reporter.setEmail("manager@bugrap.com");
        reporter.setConsistencyVersion(0);
        reporter.setAdmin(false);
        reporter.setId(2);

        mockProject.setManager(reporter);
        mockProject.setName("Project 1");
        mockProject.setConsistencyVersion(0);
        mockProject.setId(4);

        ProjectVersion projectVersion1 = new ProjectVersion();
        projectVersion1.setVersion("Version 1");
        projectVersion1.setClosed(false);
        projectVersion1.setId(5);
        projectVersion1.setConsistencyVersion(0);
        projectVersion1.setReleaseDate(null);
        projectVersion1.setProject(mockProject);
        
        Reporter admin = new Reporter();
        admin.setName("admin");
        admin.setEmail("admin@bugrap.com");
        admin.setConsistencyVersion(0);
        admin.setAdmin(false);
        admin.setId(1);
        
        Reporter manager = new Reporter();
        manager.setName("manager");
        manager.setEmail("manager@bugrap.com");
        manager.setConsistencyVersion(0);
        manager.setAdmin(false);
        manager.setId(2);
        
        Reporter developer = new Reporter();
        developer.setName("developer");
        developer.setEmail("developer@bugrap.com");
        developer.setConsistencyVersion(0);
        developer.setAdmin(false);
        developer.setId(3);
        
        Report report1 = new Report();
        report1.setAssigned(developer);
        report1.setDescription("test descirption");
        report1.setId(24);
        report1.setPriority(Report.Priority.TRIVIAL);
        report1.setVersion(projectVersion1);
        report1.setProject(mockProject);
        report1.setReportedTimestamp(new Date());
        report1.setType(Report.Type.FEATURE);
        report1.setStatus(null);
        report1.setSummary("Feature Request 65");
        report1.setTimestamp(new Date());
        
        Report report2 = new Report();
        report2.setAssigned(developer);
        report2.setDescription("test descirption");
        report2.setId(25);
        report2.setPriority(Report.Priority.TRIVIAL);
        report2.setVersion(projectVersion1);
        report2.setProject(mockProject);
        report2.setReportedTimestamp(new Date());
        report2.setType(Report.Type.FEATURE);
        report2.setStatus(null);
        report2.setSummary("Feature Request 23");
        report2.setTimestamp(new Date());
        
        projectReportsList.add(report1);
        projectReportsList.add(report2);
        return projectReportsList;
    }

    @Override
    public List<Report> filterByProjectVersion(Project project,
            ProjectVersion selectedProjectVersion) {
        List<Report> reportList = this.getAllProjectReports(project);
        if (!selectedProjectVersion.getVersion().equals("All versions")) {
                List<Report> filteredList = reportList.stream().filter(report -> report.getVersion() != null)
                                .filter(report -> selectedProjectVersion.getVersion().equals(report.getVersion().getVersion()))
                                .toList();
                return filteredList;
        }
        return reportList;
    }

    @Override
    public Integer getClosedCount(List<Report> reportList) {
        return 0;
    }

    @Override
    public Integer getUnassignedCount(List<Report> reportList) {
        return 15;
    }

    @Override
    public Integer getUnResolvedCount(List<Report> reportList) {
        return 7;
    }

    @Override
    public void saveUpdatedReportDetails(Report report) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<Report> findReportById(Long id) {
        return Optional.empty();
    }

}
