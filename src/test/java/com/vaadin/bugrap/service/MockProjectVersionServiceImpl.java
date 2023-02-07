package com.vaadin.bugrap.service;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Reporter;

public class MockProjectVersionServiceImpl implements ProjectVersionService {

    @Override
    public List<ProjectVersion> getAllProjectVersions(Project project) {
        List<ProjectVersion> projectVersionsList = new ArrayList<ProjectVersion>();

        Project mockProject = new Project();
        Reporter reporter = new Reporter();
        reporter.setName("manager");
        reporter.setEmail("manager@bugrap.com");
        reporter.setConsistencyVersion(0);
        reporter.setAdmin(false);
        reporter.setId(2);

        mockProject.setManager(reporter);
        mockProject.setName("Project 1");
        mockProject.setConsistencyVersion(0);
        mockProject.setId(4);

        ProjectVersion projectVersion1 = new ProjectVersion();
        projectVersion1.setVersion("Version 1");
        projectVersion1.setClosed(false);
        projectVersion1.setId(5);
        projectVersion1.setConsistencyVersion(0);
        projectVersion1.setReleaseDate(null);
        projectVersion1.setProject(mockProject);

        ProjectVersion projectVersion2 = new ProjectVersion();
        projectVersion2.setVersion("Version 2");
        projectVersion2.setClosed(false);
        projectVersion2.setId(6);
        projectVersion2.setConsistencyVersion(0);
        projectVersion2.setReleaseDate(null);
        projectVersion2.setProject(mockProject);

        ProjectVersion projectVersion3 = new ProjectVersion();
        projectVersion3.setVersion("Version 3");
        projectVersion3.setClosed(false);
        projectVersion3.setId(7);
        projectVersion3.setConsistencyVersion(0);
        projectVersion3.setReleaseDate(null);
        projectVersion3.setProject(mockProject);
        
        projectVersionsList.add(projectVersion1);
        projectVersionsList.add(projectVersion2);
        projectVersionsList.add(projectVersion3);
        return projectVersionsList;
    }

    @Override
    public List<ProjectVersion> getAllProjectVersionsWithAllVersions(
            Project project) {
        List<ProjectVersion> projectVersions = this
                .getAllProjectVersions(project);
        if (projectVersions.size() >= 1) {
            ProjectVersion allVersions = new ProjectVersion();
            allVersions.setVersion("All versions");
            projectVersions.add(0, allVersions);
        }
        return projectVersions;
    }

}
