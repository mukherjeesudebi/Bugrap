package com.vaadin.bugrap.service;

import java.util.List;
import java.util.Optional;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

public interface ReportService {

    public List<Report> getAllProjectReports(Project project);

    public List<Report> filterByProjectVersion(Project project,
            ProjectVersion selectedProjectVersion);

    public Integer getClosedCount(List<Report> reportList);

    public Integer getUnassignedCount(List<Report> reportList);

    public Integer getUnResolvedCount(List<Report> reportList);

    public void saveUpdatedReportDetails(Report report);

    public Optional<Report> findReportById(Long id);

}
