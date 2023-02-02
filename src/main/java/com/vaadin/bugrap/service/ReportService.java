package com.vaadin.bugrap.service;

import java.util.List;
import java.util.Optional;

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

	public Integer getClosedCount(List<Report> reportList) {
		return reportList.stream().filter(report -> report.getStatus()!=null && report.getStatus().equals(Report.Status.FIXED)).toList().size();
	}

	public Integer getUnassignedCount(List<Report> reportList) {
		return reportList.stream().filter(report -> report.getAssigned() == null).toList().size();
	}

	public Integer getUnResolvedCount(List<Report> reportList) {
		return reportList.stream().filter(report -> !(report.getStatus()!=null && report.getStatus().equals(Report.Status.FIXED))).toList()
				.size();
	}

	public void saveUpdatedReportDetails(Report report) {
		this.reportDao.saveUpdatedReportDetails(report);
	}

	public Optional<Report> findReportById(Long id) {
		return this.reportDao.findReportById(id);
	}

}
