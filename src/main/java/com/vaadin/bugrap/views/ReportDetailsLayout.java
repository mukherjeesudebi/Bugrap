package com.vaadin.bugrap.views;

import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.ProjectVersionDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class ReportDetailsLayout extends VerticalLayout {

	private Grid<Report> grid;
	private GridDataView<Report> gridDataView;
	private Binder<Report> reportBinder;
	private H6 reportSummary;
	private Select<Priority> prioritySelect;
	private Select<Type> typeSelect;
	private Select<Status> statusSelect;
	private Select<Reporter> assignedToSelect;
	private Select<ProjectVersion> projectVersionSelect;
	private Div descriptionDiv;

	private ReporterDao reporterDao;
	private ProjectVersionDao projectVersionDao;
	private Project selectedProject;

	private Button saveChangesButton = new Button("Save Changes");
	private Button revertChangesButton = new Button("Revert");
	private Report selectedReport;

	public ReportDetailsLayout(ReporterDao reporterDao, ProjectVersionDao projectVersionDao) {
		this.reporterDao = reporterDao;
		this.projectVersionDao = projectVersionDao;
		reportBinder = new Binder<>(Report.class);
	}

	public void connectGrid() {
		gridDataView = grid.getGenericDataView();
		grid.asSingleSelect().addValueChangeListener(event -> {
			selectedReport = event.getValue();
			reportSummary.setText(selectedReport.getSummary());
			descriptionDiv.setText(selectedReport.getDescription());
			reportBinder.bind(prioritySelect, Report::getPriority, Report::setPriority);
			reportBinder.bind(typeSelect, Report::getType, Report::setType);
			reportBinder.bind(statusSelect, Report::getStatus, Report::setStatus);
			reportBinder.bind(assignedToSelect, Report::getAssigned, Report::setAssigned);
			reportBinder.bind(projectVersionSelect, Report::getVersion, Report::setVersion);
			reportBinder.readBean(selectedReport);
		});
		saveChangesButton.addClickListener(event -> {
			try {
				reportBinder.writeBean(selectedReport);
				gridDataView.refreshItem(selectedReport);
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		revertChangesButton.addClickListener(event -> {
			reportBinder.readBean(selectedReport);
			gridDataView.refreshItem(selectedReport);
		});
	}

	public void createReportDetails() {
		reportSummary = new H6();
		add(reportSummary);

		HorizontalLayout reportPropertieAndAction = new HorizontalLayout();
		HorizontalLayout propertiesLayout = new HorizontalLayout();
		HorizontalLayout propertiesActionlLayout = new HorizontalLayout();

		prioritySelect = new Select<Priority>();
		prioritySelect.setItems(Stream.of(Report.Priority.values()).toList());
		prioritySelect.setLabel("Priority");
		propertiesLayout.add(prioritySelect);

		typeSelect = new Select<Type>();
		typeSelect.setItems(Stream.of(Report.Type.values()).toList());
		typeSelect.setLabel("Type");
		propertiesLayout.add(typeSelect);

		statusSelect = new Select<Status>();
		statusSelect.setItems(Stream.of(Report.Status.values()).toList());
		statusSelect.setLabel("Status");
		propertiesLayout.add(statusSelect);

		assignedToSelect = new Select<Reporter>();
		assignedToSelect.setItems(this.reporterDao.getAllReporters());
		assignedToSelect.setLabel("Assigned to");
		propertiesLayout.add(assignedToSelect);

		projectVersionSelect = new Select<ProjectVersion>();
		projectVersionSelect.setItems(this.projectVersionDao.getAllProjectVersions(this.selectedProject));
		projectVersionSelect.setLabel("Version");
		propertiesLayout.add(projectVersionSelect);

		propertiesActionlLayout.add(saveChangesButton);
		propertiesActionlLayout.add(revertChangesButton);

		reportPropertieAndAction.add(propertiesLayout);
		reportPropertieAndAction.add(propertiesActionlLayout);
		reportPropertieAndAction.setAlignItems(Alignment.BASELINE);
		reportPropertieAndAction.setJustifyContentMode(JustifyContentMode.BETWEEN);
		reportPropertieAndAction.setWidthFull();
		add(reportPropertieAndAction);

		descriptionDiv = new Div();
		add(descriptionDiv);

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

	public Select<ProjectVersion> getProjectVersionSelect() {
		return projectVersionSelect;
	}

	public void setProjectVersionSelect(Select<ProjectVersion> projectVersionSelect) {
		this.projectVersionSelect = projectVersionSelect;
	}

}
