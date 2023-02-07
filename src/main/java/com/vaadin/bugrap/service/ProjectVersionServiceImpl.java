package com.vaadin.bugrap.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.bugrap.dao.ProjectVersionDao;

@Component
public class ProjectVersionServiceImpl implements ProjectVersionService {

    private ProjectVersionDao ProjectVersionDao;

    public ProjectVersionServiceImpl(ProjectVersionDao projectVersionDao) {
        this.ProjectVersionDao = projectVersionDao;
    }

    public List<ProjectVersion> getAllProjectVersions(Project project) {
        return this.ProjectVersionDao.getAllProjectVersions(project);
    }

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
