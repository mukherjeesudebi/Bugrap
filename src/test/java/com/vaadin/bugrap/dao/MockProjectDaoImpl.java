package com.vaadin.bugrap.dao;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.Reporter;


public class MockProjectDaoImpl implements ProjectDao {

    @Override
    public List<Project> getAllProjectsList() {
        List<Project> projectList = new ArrayList<Project>();
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

        projectList.add(mockProject);
        return projectList;
    }

}
