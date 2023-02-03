package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.bugrap.components.DistributionBar;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

public class BodyLayout extends VerticalLayout {

	private ProjectVersionService projectVersionService;
	private ReportService reportService;
	private Project selectedProject;
	private ProjectVersion selectedProjectVerion;

	private Select<ProjectVersion> projectVersionsSelect = new Select<ProjectVersion>();;
	private Grid<Report> grid;

	private List<Report> reportsList;

	private ReportDetailsLayout reportDetailsLayout;
	private DistributionBar distributionBar = new DistributionBar();
	VerticalLayout reportingBlockLayout;

	public BodyLayout(ProjectVersionService projectVersionService, ReportService reportService,
			Project selectedProject) {
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
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
		HorizontalLayout functionAnsSearchLayout = new HorizontalLayout();
		HorizontalLayout functionButtonsLayout = new HorizontalLayout();
		Button bugButton = new Button("Report a bug", new Icon(VaadinIcon.BUG));
		bugButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		Button failureRequestButton = new Button("Request a failure", new Icon(VaadinIcon.LIGHTBULB));
		failureRequestButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		Button manageProjectButton = new Button("Manage Project", new Icon(VaadinIcon.SUN_O));
		manageProjectButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		functionButtonsLayout.add(bugButton, failureRequestButton, manageProjectButton);

		TextField textField = new TextField();
		textField.setClearButtonVisible(true);
		textField.setPlaceholder("Search...");
		textField.setPrefixComponent(VaadinIcon.SEARCH.create());

		functionAnsSearchLayout.add(functionButtonsLayout);
		functionAnsSearchLayout.add(textField);

		functionAnsSearchLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		functionAnsSearchLayout.setWidthFull();
		add(functionAnsSearchLayout);
	}

	public void addReportingBlock() {
		reportingBlockLayout = new VerticalLayout();
		loadProjectVersions(selectedProject);
		loadProjectVersionsWithLabel();
		addFilters();
		loadReportsGrid();
		add(reportingBlockLayout);
		reportingBlockLayout.getStyle().set("background-color", "white");

	}

	public void loadProjectVersionsWithLabel() {
		HorizontalLayout versionAndDistributionLayout = new HorizontalLayout();
		HorizontalLayout projectVersionshorizontalLayout = new HorizontalLayout();
		Div versionsLabel = new Div();
		versionsLabel.setText("Reports For");
		projectVersionshorizontalLayout.add(versionsLabel);
		projectVersionshorizontalLayout.add(projectVersionsSelect);
		projectVersionsSelect.addThemeName("bugrap-select");
		projectVersionshorizontalLayout.setAlignItems(Alignment.CENTER);
		versionAndDistributionLayout.add(projectVersionshorizontalLayout);
		versionAndDistributionLayout.add(distributionBar);
		versionAndDistributionLayout.setAlignItems(Alignment.CENTER);
		reportingBlockLayout.add(versionAndDistributionLayout);

	}

	public void loadProjectVersions(Project project) {
		List<ProjectVersion> projectVersionsList = projectVersionService.getAllProjectVersionsWithAllVersions(project);
		selectedProjectVerion = projectVersionsList.get(0);
		projectVersionsSelect.setItems(projectVersionsList);
		projectVersionsSelect.setValue(selectedProjectVerion);
		projectVersionsSelect.setItemLabelGenerator(ProjectVersion::getVersion);
		projectVersionsSelect.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				if (event.getValue().getVersion().equals("All versions")) {
					grid.getColumnByKey("version").setVisible(true);
				} else {
					grid.getColumnByKey("version").setVisible(false);
				}
				reportsList = reportService.filterByProjectVersion(selectedProject, event.getValue());
				grid.setItems(reportsList);
				distributionBar.setClosed(reportService.getClosedCount(reportsList));
				distributionBar.setUnAssigned(reportService.getUnassignedCount(reportsList));
				distributionBar.setUnResolved(reportService.getUnResolvedCount(reportsList));
				distributionBar.setWidthLayout();
			}
		});

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
		filtersLayout.setWidth("50%");
		reportingBlockLayout.add(filtersLayout);
	}

	public void loadReportsGrid() {
		grid = new Grid<>(Report.class, false);
		grid.addThemeName("bugrap-grid");
		grid.setSelectionMode(SelectionMode.MULTI);
		//grid.addColumn(createCheckBoxRenderer()).setKey("reportCheckbox");
		grid.addColumn(Report::getVersion).setHeader("Version").setKey("version").setSortable(true);
		grid.addColumn(createPriorityRenderer()).setHeader("Priority").setSortable(true)
				.setComparator(Report::getPriority);
		grid.addColumn(Report::getType).setHeader("Type").setSortable(true);
		grid.addColumn(Report::getSummary).setHeader("Summary").setSortable(true);
		grid.addColumn(Report::getAssigned).setHeader("Assigned to").setSortable(true);
		grid.addColumn(Report::getTimestamp).setHeader("Last modified").setSortable(true);
		grid.addColumn(Report::getReportedTimestamp).setHeader("Reported").setSortable(true);
		loadReports(selectedProject);
		reportingBlockLayout.add(grid);
		if (selectedProjectVerion.getVersion().equals("All versions")) {
			grid.getColumnByKey("version").setVisible(true);
		} else {
			grid.getColumnByKey("version").setVisible(false);
		}
	}

	public ComponentRenderer<Checkbox, Report> createCheckBoxRenderer() {
		return new ComponentRenderer<>(report -> {
			return new Checkbox();
		});
	}

	public void loadReports(Project project) {
		reportsList = reportService.getAllProjectReports(project);
		distributionBar.setClosed(reportService.getClosedCount(reportsList));
		distributionBar.setUnAssigned(reportService.getUnassignedCount(reportsList));
		distributionBar.setUnResolved(reportService.getUnResolvedCount(reportsList));
		distributionBar.setWidthLayout();
		grid.setItems(reportsList);
		System.out.println(reportsList.get(0).getPriority());
	}

	private Renderer<Report> createPriorityRenderer() {
		return LitRenderer.<Report>of("<report-priority priority=\"${item.priority}\"></report-priority>")
				.withProperty("priority", Report::getPriority);
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

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}
}
