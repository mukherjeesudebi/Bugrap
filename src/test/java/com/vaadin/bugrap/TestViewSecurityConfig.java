package com.vaadin.bugrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.MockProjectDaoImpl;
import com.vaadin.bugrap.dao.MockReporterDaoImpl;
import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.MockSecurityServiceImpl;
import com.vaadin.bugrap.security.SecurityService;
import com.vaadin.bugrap.service.MockProjectVersionServiceImpl;
import com.vaadin.bugrap.service.MockReportServiceImpl;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;

@Configuration
public class TestViewSecurityConfig {
    @Bean
    ProjectDao getProjectdDao() {
        return new MockProjectDaoImpl();
    }

    @Bean
    ProjectVersionService getProjectVersionService() {
        return new MockProjectVersionServiceImpl();
    }

    @Bean
    ReportService getReportService() {
        return new MockReportServiceImpl();
    }

    @Bean
    ReporterDao getReporterDao() {
        return new MockReporterDaoImpl();
    }

    @Bean
    SecurityService getSecurityService() {
        return new MockSecurityServiceImpl();
    }

    @Bean
    Reporter mockAuthenticatedUser() {
        return new Reporter();
    }
}
