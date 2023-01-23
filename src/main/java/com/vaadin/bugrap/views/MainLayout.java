package com.vaadin.bugrap.views;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private ProjectDao projectDao;
	
	public MainLayout(ProjectDao projectDao) {
		this.projectDao = projectDao;		
		addHeader();
	}
	
	public void addHeader() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		Select<String> select = new Select<>();
		select.setItems(projectDao.getAllProjectNames());
		select.setValue(projectDao.getAllProjectNames().get(0));
		horizontalLayout.add(select);
		
		add(horizontalLayout);
	}
}
