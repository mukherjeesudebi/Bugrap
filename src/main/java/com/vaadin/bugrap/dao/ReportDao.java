package com.vaadin.bugrap.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.spring.ReportRepository;

import com.vaadin.bugrap.repository.BugrapReportRepository;

@Component
public class ReportDao {
	private ReportRepository reportRepository;
	private BugrapReportRepository bugrapReportRepository;

	public ReportDao(ReportRepository reportRepository,BugrapReportRepository bugrapReportRepository) {
		this.reportRepository = reportRepository;
		this.bugrapReportRepository = bugrapReportRepository;
	}

	public List<Report> getAllProjectReports(Project project) {
		return reportRepository.findAllByProject(project);
	}

	public void saveUpdatedReportDetails(Report report) {
		this.reportRepository.save(report);
	}

	public Optional<Report> findReportById(Long id) {
		return this.bugrapReportRepository.findById(id);
	}
}
