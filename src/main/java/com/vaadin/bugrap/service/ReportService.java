package com.vaadin.bugrap.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.bugrap.dao.ReportDao;

@Component
public class ReportService {

	private ReportDao reportDao;

	public ReportService(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	

	public List<Report> getAllProjectReports(Project project) {
		return reportDao.getAllProjectReports(project);
	}

	public List<Report> filterByProjectVersion(Project project, ProjectVersion selectedProjectVersion) {
		List<Report> reportList = this.getAllProjectReports(project);
		if (!selectedProjectVersion.getVersion().equals("All versions")) {
			List<Report> filteredList = reportList.stream().filter(report -> report.getVersion() != null)
					.filter(report -> selectedProjectVersion.getVersion().equals(report.getVersion().getVersion()))
					.toList();
			return filteredList;
		}
		return reportList;
	}

	public void saveUpdatedReportDetails(Report report) {
		this.reportDao.saveUpdatedReportDetails(report);
	}
	
}
