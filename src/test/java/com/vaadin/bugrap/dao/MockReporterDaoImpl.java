package com.vaadin.bugrap.dao;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Reporter;

public class MockReporterDaoImpl implements ReporterDao {

    @Override
    public List<Reporter> getAllReporters() {
        List<Reporter> reportersList = new ArrayList<Reporter>();
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
        
        reportersList.add(admin);
        reportersList.add(manager);
        reportersList.add(developer);
        return reportersList;
    }

    @Override
    public Reporter findReporter(String name, String password) {
        Reporter admin = new Reporter();
        admin.setName("admin");
        admin.setEmail("admin@bugrap.com");
        admin.setConsistencyVersion(0);
        admin.setAdmin(false);
        admin.setId(1);
        return admin;
    }

    @Override
    public Reporter findReporterByName(String name, String password) {
        Reporter admin = new Reporter();
        admin.setName("admin");
        admin.setEmail("admin@bugrap.com");
        admin.setConsistencyVersion(0);
        admin.setAdmin(false);
        admin.setId(1);
        return admin;
    }

}
