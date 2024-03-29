package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.spring.ProjectRepository;

@Component
public class ProjectDaoImpl implements ProjectDao{
    private ProjectRepository projectRepository;
    //private List<Project> allProjectsList;
    
    public ProjectDaoImpl(ProjectRepository projectRepository) {
            this.projectRepository = projectRepository;
    }
    
    public List<Project> getAllProjectsList() {
            return this.projectRepository.findAll();
    }
}
