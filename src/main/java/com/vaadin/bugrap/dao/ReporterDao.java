package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Reporter;
import org.vaadin.bugrap.domain.spring.ReporterRepository;

@Component
public class ReporterDao {

	private ReporterRepository reporterRepository;
	
	public ReporterDao(ReporterRepository reporterRepository) {
		this.reporterRepository = reporterRepository;
	}
	
	public List<Reporter> getAllReporters() {
		return this.reporterRepository.findAll();
	}
	
	public Reporter findReporter(String name,String password) {
		return this.reporterRepository.getByNameAndPassword(name, password);
	}
	
	public Reporter findReporterByName(String name,String password) {
		return this.reporterRepository.getByNameOrEmail(name,null);
	}
}
