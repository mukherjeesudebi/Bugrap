package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.spring.ProjectVersionRepository;

@Component
public class ProjectVersionDao {
	private ProjectVersionRepository projectVersionRepository;
	
	public ProjectVersionDao(ProjectVersionRepository projectVersionRepository) {
		this.projectVersionRepository = projectVersionRepository;
	}
	
	public List<String> getAllProjectVersions(Project project) {
		return this.projectVersionRepository.findAllByProject(project).stream().map(v -> v.getVersion()).toList();
	}
}
