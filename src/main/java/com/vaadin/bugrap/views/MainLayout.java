package com.vaadin.bugrap.views;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
		HorizontalLayout headerHorizontalLayout = new HorizontalLayout();
		
		Select<String> select = new Select<>();
		select.setItems(projectDao.getAllProjectNames());
		select.setValue(projectDao.getAllProjectNames().get(0));
		headerHorizontalLayout.add(select);
		
		HorizontalLayout headerHorizontalLayoutRight = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		H6 userName = new H6("Marc Manager");
		Icon powerOffIcon = new Icon(VaadinIcon.POWER_OFF);
		headerHorizontalLayoutRight.add(userIcon,userName,powerOffIcon);
		headerHorizontalLayout.add(headerHorizontalLayoutRight);
		
		headerHorizontalLayout.setWidthFull();
		headerHorizontalLayout.setAlignItems(Alignment.STRETCH);
		add(headerHorizontalLayout);
	}
}
