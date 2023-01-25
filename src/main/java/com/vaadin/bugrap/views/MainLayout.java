package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.addons.searchbox.SearchBox;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.bugrap.dao.ProjectDao;
import com.vaadin.bugrap.dao.ProjectVersionDao;
import com.vaadin.bugrap.dao.ReportDao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private ProjectDao projectDao;
	private ProjectVersionDao projectVersionDao;
	private ReportDao reportDao;
	
	private HeaderLayout headerLayout;
	private Select<Project> projectSelect;
	private Select<ProjectVersion> projectVersionsSelect;
	private Grid<Report> grid = new Grid<>(Report.class, false);

	public MainLayout(ProjectDao projectDao, ProjectVersionDao projectVersionDao, ReportDao reportDao) {
		this.projectDao = projectDao;
		this.projectVersionDao = projectVersionDao;
		this.reportDao = reportDao;

		projectSelect = new Select<Project>();
		projectVersionsSelect = new Select<ProjectVersion>();

		addHeader();
		addBody();
	}

	public void addHeader() {
		headerLayout = new HeaderLayout(projectDao);
		add(headerLayout);
	}

	public void addBody() {
		VerticalLayout verticalBodyLayout = new VerticalLayout();
		verticalBodyLayout.setHeightFull();
		verticalBodyLayout.setWidthFull();
		verticalBodyLayout.getStyle().set("background-color", "#f0f0f0");

		addFunctionAndSearch(verticalBodyLayout);
		addReportingBlock(verticalBodyLayout, projectDao.getAllProjectsList().get(0));

		add(verticalBodyLayout);
	}

	public void addFunctionAndSearch(VerticalLayout verticalBodyLayout) {
		addFunctionButtons(verticalBodyLayout);
		addSearchComponent(verticalBodyLayout);
	}

	public void addFunctionButtons(VerticalLayout verticalBodyLayout) {
		HorizontalLayout functionButtonsLayout = new HorizontalLayout();
		Button bugButton = new Button("Report a bug", new Icon(VaadinIcon.BUG));
		bugButton.getStyle().set("background-color", "white");
		bugButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");

		Button failureRequestButton = new Button("Request a failure", new Icon(VaadinIcon.LIGHTBULB));
		failureRequestButton.getStyle().set("background-color", "white");
		failureRequestButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");

		Button manageProjectButton = new Button("Manage Project", new Icon(VaadinIcon.SUN_O));
		manageProjectButton.getStyle().set("background-color", "white");
		manageProjectButton.getStyle().set("box-shadow", "rgb(99 99 99 / 25%) 0px 2px 8px -2px");

		functionButtonsLayout.add(bugButton, failureRequestButton, manageProjectButton);
		verticalBodyLayout.add(functionButtonsLayout);

	}

	public void addSearchComponent(VerticalLayout verticalBodyLayout) {
		SearchBox searchBox = new SearchBox("Search", SearchBox.ButtonPosition.RIGHT);
	}

	public void addReportingBlock(VerticalLayout verticalBodyLayout, Project selectedProject) {
		loadProjectVersions(selectedProject);
		loadReportsGrid(selectedProject);

		verticalBodyLayout.add(projectVersionsSelect);
		addFilters(verticalBodyLayout);
		verticalBodyLayout.add(grid);

	}

	public void loadProjectVersions(Project project) {
		List<ProjectVersion> projectVersionsList = projectVersionDao.getAllProjectVersions(project);
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

		MenuBar assigneeTabsMenu = new MenuBar();
		assigneeTabsMenu.addItem("Only Me");
		assigneeTabsMenu.addItem("Everyone");

		assigneeDiv.add(assigneeDivText, assigneeTabsMenu);

		Div statusDiv = new Div();
		statusDiv.getStyle().set("display", "flex");
		statusDiv.getStyle().set("align-items", "center");

		Div statusDivText = new Div();
		statusDivText.setText("Status");

		MenuBar statusTabsMenu = new MenuBar();
		statusTabsMenu.addItem("Open");
		statusTabsMenu.addItem("All kinds");
		statusTabsMenu.addItem("Custom...");

		assigneeDiv.add(assigneeDivText, assigneeTabsMenu);
		statusDiv.add(statusDivText, statusTabsMenu);
		filtersLayout.add(assigneeDiv, statusDiv);
		verticalBodyLayout.add(filtersLayout);
	}

	public void loadReportsGrid(Project project) {
		grid.addColumn(Report::getPriority).setHeader("Priority").setSortable(true);
		grid.addColumn(Report::getType).setHeader("Type").setSortable(true);
		grid.addColumn(Report::getSummary).setHeader("Summary").setSortable(true);
		grid.addColumn(Report::getAssigned).setHeader("Assigned to").setSortable(true);
		grid.addColumn(Report::getTimestamp).setHeader("Last modified").setSortable(true);
		grid.addColumn(Report::getReportedTimestamp).setHeader("Reported").setSortable(true);
		loadReports(project);
	}

	public void loadReports(Project project) {
		List<Report> reportsList = reportDao.getAllProjectReports(project);
		grid.setItems(reportsList);
	}
}
