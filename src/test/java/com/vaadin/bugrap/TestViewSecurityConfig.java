package com.vaadin.bugrap;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.CommentDao;
import com.vaadin.bugrap.dao.MockCommentDaoImpl;
import com.vaadin.bugrap.dao.MockProjectDaoImpl;
import com.vaadin.bugrap.dao.MockReporterDaoImpl;
import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.AuthenticatedUser;
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
    CommentDao getCommentDao() {
        return new MockCommentDaoImpl();
    }

    @Bean
    AuthenticatedUser mockAuthenticatedUser() {
        return new MockAuthenticatedUser();
    }

    // Dummy authenticated user is needed to satifisfy injection, user is
    // actually faked by @WithMockUser
    public class MockAuthenticatedUser implements AuthenticatedUser {
        private Optional<Authentication> getAuthentication() {
            SecurityContext context = SecurityContextHolder.getContext();
            return Optional.ofNullable(context.getAuthentication()).filter(
                    authentication -> !(authentication instanceof AnonymousAuthenticationToken));
        }

        @Override
        public Optional<Reporter> get() {
            if (!getAuthentication().isEmpty()) {
                Authentication auth = getAuthentication().get();
                Reporter reporter = new Reporter();
                reporter.setName(auth.getName());
                return Optional.of(reporter);
            } else {
                return Optional.empty();
            }
        }

        @Override
        public void logout() {
        }

    }
}
