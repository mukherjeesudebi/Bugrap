package com.vaadin.bugrap.views;

import org.vaadin.bugrap.domain.entities.Project;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class HeaderLayout extends HorizontalLayout {
	
	private ProjectDao projectDao;
	private Select<Project> projectSelect;

	public HeaderLayout(ProjectDao projectDao) {
		this.projectDao = projectDao;
		createHeader();
	}
	
	public void createHeader(){
		projectSelect = new Select<Project>();
		projectSelect.setItems(projectDao.getAllProjectsList());
		projectSelect.setItemLabelGenerator(Project::getName);
		projectSelect.setValue(projectDao.getAllProjectsList().get(0));
		/*
		 * projectSelect.addValueChangeListener(event -> {
		 * loadProjectVersions(event.getValue()); loadReports(event.getValue()); });
		 */
		add(projectSelect);

		HorizontalLayout headerHorizontalLayoutRight = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		Div userName = new Div();
		userName.setText("Marc Manager");
		Icon powerOffIcon = new Icon(VaadinIcon.POWER_OFF);

		/*
		 * TextField searchField = new TextField();
		 * searchField.setSuffixComponent(VaadinIcon.SEARCH.create());
		 */

		headerHorizontalLayoutRight.add(userIcon, userName, powerOffIcon);
		headerHorizontalLayoutRight.addClassName(LumoUtility.TextColor.PRIMARY);
		add(headerHorizontalLayoutRight);

		setWidthFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.BETWEEN);
//		headerHorizontalLayout.getStyle().set("box-shadow", "0 4px 7px -2px gray");
		addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.Padding.MEDIUM);
		setHeightFull();

	
	}
}
