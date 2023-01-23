package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.spring.ProjectRepository;


@Component
public class ProjectDao {

	private ProjectRepository projectRepository;
	//private List<Project> allProjectsList;
	
	public ProjectDao(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	
	public List<Project> getAllProjectsList() {
		return this.projectRepository.findAll();
	}
}
