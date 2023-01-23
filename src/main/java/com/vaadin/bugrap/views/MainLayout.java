package com.vaadin.bugrap.views;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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
		addBody();
	}
	
	public void addHeader() {
		HorizontalLayout headerHorizontalLayout = new HorizontalLayout();
		
		Select<String> select = new Select<>();
		select.setItems(projectDao.getAllProjectNames());
		select.setValue(projectDao.getAllProjectNames().get(0));
		headerHorizontalLayout.add(select);
		
		HorizontalLayout headerHorizontalLayoutRight = new HorizontalLayout();
		Icon userIcon = new Icon(VaadinIcon.USER);
		Div userName = new Div();
		userName.setText("Marc Manager");
		Icon powerOffIcon = new Icon(VaadinIcon.POWER_OFF);
		headerHorizontalLayoutRight.add(userIcon,userName,powerOffIcon);
		headerHorizontalLayoutRight.getStyle().set("color", "#3c86ce");
		headerHorizontalLayout.add(headerHorizontalLayoutRight);
		
		headerHorizontalLayout.setWidthFull();
		headerHorizontalLayout.setAlignItems(Alignment.CENTER);
		headerHorizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		headerHorizontalLayout.getStyle().set("box-shadow", "0 4px 7px -2px gray");
		add(headerHorizontalLayout);
		setHeightFull();
	}
	
	public void addBody() {
		VerticalLayout verticalBodyLayout = new VerticalLayout();
		verticalBodyLayout.setHeightFull();
		verticalBodyLayout.setWidthFull();
		verticalBodyLayout.getStyle().set("background-color", "#e8e5e5");
		addFunctionButtons(verticalBodyLayout);
		add(verticalBodyLayout);
	}
	
	public void addFunctionButtons(VerticalLayout verticalBodyLayout) {
		HorizontalLayout functionButtonsLayout = new HorizontalLayout();
		Button bugButton = new Button("Report a bug",new Icon(VaadinIcon.BUG)); 
		Button failureRequestButton = new Button("Request a failure",new Icon(VaadinIcon.LIGHTBULB)); 
		Button manageProjectButton = new Button("Manage Project",new Icon(VaadinIcon.SUN_O)); 
		
		functionButtonsLayout.add(bugButton,failureRequestButton,manageProjectButton);
		verticalBodyLayout.add(functionButtonsLayout);
		
		
	}
}
