package com.vaadin.bugrap.dao;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Reporter;

public interface ReporterDao {

    public List<Reporter> getAllReporters();

    public Reporter findReporter(String name, String password);

    public Reporter findReporterByName(String name, String password);
}
