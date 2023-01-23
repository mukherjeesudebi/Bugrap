package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
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
}
