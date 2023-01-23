package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.spring.ProjectRepository;


@Component
public class ProjectDao {

	private ProjectRepository projectRepository;
	
	public ProjectDao(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	
	public List<String> getAllProjectNames() {
		return this.projectRepository.findAll().stream().map(p->p.getName()).toList();
	}
}
