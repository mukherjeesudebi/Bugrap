package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Project;

@Component
public interface ProjectDao {

    public List<Project> getAllProjectsList();
}
