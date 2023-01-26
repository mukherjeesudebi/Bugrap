package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.spring.ReportRepository;

@Component
public class ReportDao {
	private ReportRepository reportRepository;

	public ReportDao(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	public List<Report> getAllProjectReports(Project project) {
		return reportRepository.findAllByProject(project);
	}

	public List<Report> filterByProjectVersion(Project project, ProjectVersion selectedProjectVersion) {
		List<Report> reportList = this.getAllProjectReports(project);
		if (!selectedProjectVersion.getVersion().equals("All versions")) {
			List<Report> filteredList = reportList.stream().filter(report -> report.getVersion() != null)
					.filter(report -> selectedProjectVersion.getVersion().equals(report.getVersion().getVersion()))
					.toList();
			filteredList.stream().forEach(report-> System.out.println(report.getVersion().getVersion()));
			return filteredList;
		}
		return reportList;
	}

}
