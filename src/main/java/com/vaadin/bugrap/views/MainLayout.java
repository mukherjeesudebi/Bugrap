package com.vaadin.bugrap.views;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private H2 viewTitle;
	private ProjectDao projectDao;
	
	public MainLayout(ProjectDao projectDao) {
		this.projectDao = projectDao;
		viewTitle = new H2("Hello Sudebi");
		add(viewTitle);
		
		projectDao.getAllProjectNames().stream().forEach(p-> System.out.println(p));
	}
}
