package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.addons.searchbox.SearchBox;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ProjectVersionDao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private ProjectDao projectDao;
	private ProjectVersionDao projectVersionDao;
	private Select<ProjectVersion> projectVersionsSelect;
	
	public MainLayout(ProjectDao projectDao,ProjectVersionDao projectVersionDao) {
		this.projectDao = projectDao;	
		this.projectVersionDao = projectVersionDao;
		addHeader();
		addBody();
	}
	
	public void addHeader() {
		HorizontalLayout headerHorizontalLayout = new HorizontalLayout();
		
		Select<Project> select = new Select<Project>();
		select.setItems(projectDao.getAllProjectsList());
		select.setItemLabelGenerator(Project::getName);
		select.setValue(projectDao.getAllProjectsList().get(0));
		select.addValueChangeListener(event -> loadProjectVersions(event.getValue()));
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
		verticalBodyLayout.getStyle().set("background-color", "#f0f0f0");
		
		addFunctionAndSearch(verticalBodyLayout);		
		addReportingBlock(verticalBodyLayout,projectDao.getAllProjectsList().get(0));
		
		add(verticalBodyLayout);
	}
	
	public void addFunctionAndSearch(VerticalLayout verticalBodyLayout) {
		addFunctionButtons(verticalBodyLayout);
		addSearchComponent(verticalBodyLayout);
	}
	
	public void addFunctionButtons(VerticalLayout verticalBodyLayout) {
		HorizontalLayout functionButtonsLayout = new HorizontalLayout();
		Button bugButton = new Button("Report a bug",new Icon(VaadinIcon.BUG)); 
		bugButton.getStyle().set("background-color", "white");
		bugButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");
		
		Button failureRequestButton = new Button("Request a failure",new Icon(VaadinIcon.LIGHTBULB)); 
		failureRequestButton.getStyle().set("background-color", "white");
		failureRequestButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");
		
		
		Button manageProjectButton = new Button("Manage Project",new Icon(VaadinIcon.SUN_O)); 
		manageProjectButton.getStyle().set("background-color", "white");
		manageProjectButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");
		
		functionButtonsLayout.add(bugButton,failureRequestButton,manageProjectButton);
		verticalBodyLayout.add(functionButtonsLayout);
		
		
	}
	
	public void addSearchComponent(VerticalLayout verticalBodyLayout) {
		SearchBox searchBox = new SearchBox("Search", SearchBox.ButtonPosition.RIGHT);
	}
	
	public void addReportingBlock(VerticalLayout verticalBodyLayout,Project selectedProject){
		loadProjectVersions(selectedProject);
		verticalBodyLayout.add(projectVersionsSelect);
		addFilters(verticalBodyLayout);
		
	}
	
	public void loadProjectVersions(Project project) {
        List<ProjectVersion> projectVersionsList = projectVersionDao.getAllProjectVersions(project);
		projectVersionsSelect = new Select<ProjectVersion>();
		projectVersionsSelect.setLabel("Reports For");
		projectVersionsSelect.setItems(projectVersionsList);
		projectVersionsSelect.setValue(projectVersionsList.get(0));
		projectVersionsSelect.setItemLabelGenerator(ProjectVersion::getVersion);
		
	}
	
	public void addFilters(VerticalLayout verticalBodyLayout) {
		HorizontalLayout filtersLayout = new HorizontalLayout();
		
		Div assigneeDiv = new Div();
		assigneeDiv.getStyle().set("display", "flex");
		assigneeDiv.getStyle().set("align-items", "center");
		
		Div assigneeDivText = new Div();
		assigneeDivText.setText("Assignees");
		
		Tab onlyMe = new Tab("Only Me");
		Tab everyone = new Tab("Everyone");
		Tabs assigneeTabs = new Tabs(onlyMe, everyone);
		assigneeTabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
		

		assigneeDiv.add(assigneeDivText,assigneeTabs);
		
		Div statusDiv = new Div();
		statusDiv.getStyle().set("display", "flex");
		statusDiv.getStyle().set("align-items", "center");
		
		Div statusDivText = new Div();
		statusDivText.setText("Status");
		
		Tab open = new Tab("Open");
		Tab allKinds = new Tab("All kinds");
		Tab custom = new Tab("Custom...");
		Tabs statusTabs = new Tabs(open, allKinds,custom);
		statusTabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
		

		assigneeDiv.add(assigneeDivText,assigneeTabs);
		statusDiv.add(statusDivText,statusTabs);
		filtersLayout.add(assigneeDiv,statusDiv);
		verticalBodyLayout.add(filtersLayout);
	}
}
