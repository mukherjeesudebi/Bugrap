package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

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

public class BodyLayout extends VerticalLayout {

	private ProjectVersionDao projectVersionDao;
	private ReportDao reportDao;
	private Project selectedProject;

	private Select<ProjectVersion> projectVersionsSelect;
	private Grid<Report> grid;


	private ReportDetailsLayout reportDetailsLayout;

	public BodyLayout(ProjectVersionDao projectVersionDao, ReportDao reportDao, Project selectedProject) {
		this.projectVersionDao = projectVersionDao;
		this.reportDao = reportDao;
		this.selectedProject = selectedProject;
		createBody();
	}

	public void createBody() {
		setHeightFull();
		setWidthFull();
		getStyle().set("background-color", "#f0f0f0");

		addFunctionAndSearch();
		addReportingBlock();
	}

	public void addFunctionAndSearch() {
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
		add(functionButtonsLayout);
	}

	public void addReportingBlock() {
		loadProjectVersions(selectedProject);
		addFilters();
		loadReportsGrid();

	}

	public void loadProjectVersions(Project project) {
		projectVersionsSelect = new Select<ProjectVersion>();
		List<ProjectVersion> projectVersionsList = projectVersionDao.getAllProjectVersions(project);
		projectVersionsSelect.setLabel("Reports For");
		projectVersionsSelect.setItems(projectVersionsList);
		projectVersionsSelect.setValue(projectVersionsList.get(0));
		projectVersionsSelect.setItemLabelGenerator(ProjectVersion::getVersion);
		add(projectVersionsSelect);

	}

	public void addFilters() {
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
		add(filtersLayout);
	}

	public void loadReportsGrid() {
		grid = new Grid<>(Report.class, false);
		grid.addColumn(Report::getPriority).setHeader("Priority").setSortable(true);
		grid.addColumn(Report::getType).setHeader("Type").setSortable(true);
		grid.addColumn(Report::getSummary).setHeader("Summary").setSortable(true);
		grid.addColumn(Report::getAssigned).setHeader("Assigned to").setSortable(true);
		grid.addColumn(Report::getTimestamp).setHeader("Last modified").setSortable(true);
		grid.addColumn(Report::getReportedTimestamp).setHeader("Reported").setSortable(true);
		loadReports(selectedProject);
		add(grid);
	}

	public void loadReports(Project project) {
		List<Report> reportsList = reportDao.getAllProjectReports(project);
		grid.setItems(reportsList);
	}

	public ReportDetailsLayout getReportDetailsLayout() {
		return reportDetailsLayout;
	}

	public void setReportDetailsLayout(ReportDetailsLayout reportDetailsLayout) {
		this.reportDetailsLayout = reportDetailsLayout;
	}
	
	public Grid<Report> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Report> grid) {
		this.grid = grid;
	}
}
