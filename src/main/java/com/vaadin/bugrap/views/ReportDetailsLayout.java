package com.vaadin.bugrap.views;

import java.util.Set;
import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
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
	private Select<ProjectVersion> reportprojectVersionSelect;

	private ReporterDao reporterDao;
	private Project selectedProject;

	private ProjectVersionService projectVersionService;
	private ReportService reportService;

	Icon revertIcon = new Icon("lumo", "reload");
	private Button saveChangesButton = new Button("Save Changes");
	private Button revertChangesButton = new Button("Revert", revertIcon);
	private Report selectedReport;
	private Button openButton;
	private Set<Report> selectedReports;

	public ReportDetailsLayout(ReporterDao reporterDao, ProjectVersionService projectVersionService,
			ReportService reportService) {
		this.reporterDao = reporterDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		reportBinder = new Binder<>(Report.class);
	}

	public void connectGrid() {
		gridDataView = grid.getGenericDataView();
		grid.asMultiSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				selectedReports = event.getValue();
				if (selectedReports.size() == 1) {
					selectedReport = selectedReports.stream().toList().get(0);
					reportSummary.setText(selectedReport.getSummary());
					reportBinder.bind(prioritySelect, Report::getPriority, Report::setPriority);
					reportBinder.bind(typeSelect, Report::getType, Report::setType);
					reportBinder.bind(statusSelect, Report::getStatus, Report::setStatus);
					reportBinder.bind(assignedToSelect, Report::getAssigned, Report::setAssigned);
					reportBinder.bind(reportprojectVersionSelect, Report::getVersion, Report::setVersion);
					reportBinder.readBean(selectedReport);
					openButton.setVisible(true);
				} else {
					int count = selectedReports.size();
					reportSummary.setText(count + " reports Selected.");
					reportBinder.readBean(null);
					openButton.setVisible(false);
				}
			}
		});
		grid.addItemClickListener(e -> {
			grid.asMultiSelect().select(e.getItem());
		});
		saveChangesButton.addClickListener(event -> {
			try {
				for (Report report : selectedReports) {
					reportBinder.writeBean(report);
					this.reportService.saveUpdatedReportDetails(report);
					gridDataView.refreshItem(report);
				}
				Notification successNotification = Notification.show("Report Details Saved Successfully", 3000,
						Position.MIDDLE);
				successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				reportBinder.writeBean(null);
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		revertChangesButton.addClickListener(event -> {
			for (Report report : selectedReports) {
				reportBinder.readBean(report);
				gridDataView.refreshItem(report);
			}
		});
	}

	public void createReportDetails() {
		HorizontalLayout summaryWithOpen = new HorizontalLayout();
		summaryWithOpen.setWidthFull();
		reportSummary = new H6();
		openButton = new Button("Open", new Icon(VaadinIcon.EXTERNAL_LINK));
		openButton.addThemeName("bugrap-button-link");
		openButton.addClickListener(e -> {
			String url = "/reportDetails/" + selectedReport.getId();
			UI.getCurrent().getPage().open(url);

		});
		summaryWithOpen.add(reportSummary);
		summaryWithOpen.add(openButton);
		summaryWithOpen.setJustifyContentMode(JustifyContentMode.BETWEEN);
		add(summaryWithOpen);

		HorizontalLayout reportPropertieAndAction = new HorizontalLayout();
		HorizontalLayout propertiesLayout = new HorizontalLayout();
		HorizontalLayout propertiesActionlLayout = new HorizontalLayout();

		prioritySelect = new Select<Priority>();
		prioritySelect.setItems(Stream.of(Report.Priority.values()).toList());
		prioritySelect.setLabel("Priority");
		prioritySelect.addThemeName("bugrap-report-select");
		propertiesLayout.add(prioritySelect);

		typeSelect = new Select<Type>();
		typeSelect.setItems(Stream.of(Report.Type.values()).toList());
		typeSelect.setLabel("Type");
		typeSelect.addThemeName("bugrap-report-select");
		propertiesLayout.add(typeSelect);

		statusSelect = new Select<Status>();
		statusSelect.setItems(Stream.of(Report.Status.values()).toList());
		statusSelect.setLabel("Status");
		statusSelect.addThemeName("bugrap-report-select");
		propertiesLayout.add(statusSelect);

		assignedToSelect = new Select<Reporter>();
		assignedToSelect.setItems(this.reporterDao.getAllReporters());
		assignedToSelect.setLabel("Assigned to");
		assignedToSelect.addThemeName("bugrap-report-select");
		propertiesLayout.add(assignedToSelect);

		reportprojectVersionSelect = new Select<ProjectVersion>();
		reportprojectVersionSelect.setItems(this.projectVersionService.getAllProjectVersions(this.selectedProject));
		reportprojectVersionSelect.setLabel("Version");
		reportprojectVersionSelect.addThemeName("bugrap-report-select");
		propertiesLayout.add(reportprojectVersionSelect);

		propertiesActionlLayout.add(saveChangesButton);
		saveChangesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		propertiesActionlLayout.add(revertChangesButton);
		revertChangesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		reportPropertieAndAction.add(propertiesLayout);
		reportPropertieAndAction.add(propertiesActionlLayout);
		reportPropertieAndAction.setAlignItems(Alignment.BASELINE);
		reportPropertieAndAction.setJustifyContentMode(JustifyContentMode.BETWEEN);
		reportPropertieAndAction.setWidthFull();
		add(reportPropertieAndAction);

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

	public Select<ProjectVersion> getReportprojectVersionSelect() {
		return reportprojectVersionSelect;
	}

	public void setReportprojectVersionSelect(Select<ProjectVersion> reportprojectVersionSelect) {
		this.reportprojectVersionSelect = reportprojectVersionSelect;
	}

}
