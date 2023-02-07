package com.vaadin.bugrap.service;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

public interface ProjectVersionService {

    public List<ProjectVersion> getAllProjectVersions(Project project);

    public List<ProjectVersion> getAllProjectVersionsWithAllVersions(
            Project project);

}
