package com.vaadin.bugrap.views;

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
import com.vaadin.flow.component.ComponentUtil;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.server.BrowserWindowOpener;

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
	private Div descriptionDiv;

	private ReporterDao reporterDao;
	private Project selectedProject;

	private ProjectVersionService projectVersionService;
	private ReportService reportService;

	private Button saveChangesButton = new Button("Save Changes");
	private Button revertChangesButton = new Button("Revert");
	private Report selectedReport;

	public ReportDetailsLayout(ReporterDao reporterDao, ProjectVersionService projectVersionService,
			ReportService reportService) {
		this.reporterDao = reporterDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		reportBinder = new Binder<>(Report.class);
	}

	public void connectGrid() {
		gridDataView = grid.getGenericDataView();
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				selectedReport = event.getValue();
				reportSummary.setText(selectedReport.getSummary());
				descriptionDiv.setText(selectedReport.getDescription());
				reportBinder.bind(prioritySelect, Report::getPriority, Report::setPriority);
				reportBinder.bind(typeSelect, Report::getType, Report::setType);
				reportBinder.bind(statusSelect, Report::getStatus, Report::setStatus);
				reportBinder.bind(assignedToSelect, Report::getAssigned, Report::setAssigned);
				reportBinder.bind(reportprojectVersionSelect, Report::getVersion, Report::setVersion);
				reportBinder.readBean(selectedReport);
			}
		});
		saveChangesButton.addClickListener(event -> {
			try {
				reportBinder.writeBean(selectedReport);
				this.reportService.saveUpdatedReportDetails(selectedReport);
				gridDataView.refreshItem(selectedReport);
				Notification successNotification = Notification.show("Report Details Saved Successfully", 3000,
						Position.MIDDLE);
				successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
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
		HorizontalLayout summaryWithOpen = new HorizontalLayout();
		summaryWithOpen.setWidthFull();
		reportSummary = new H6();
		Button openButton = new Button("Open", new Icon(VaadinIcon.EXTERNAL_LINK));
		openButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		openButton.addClickListener(e -> {
			/*
			 * UI.getCurrent().navigate(ReportDetailsSeparateLayout.class) .ifPresent(report
			 * -> report.setReport(selectedReport));
			 */
			//ComponentUtil.setData(UI.getCurrent().getParent().get(), Report.class, selectedReport);
			//VaadinSession.getCurrent().setAttribute("Report",selectedReport);
			String url = "/reportDetails/"+selectedReport.getId();
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

		reportprojectVersionSelect = new Select<ProjectVersion>();
		reportprojectVersionSelect.setItems(this.projectVersionService.getAllProjectVersions(this.selectedProject));
		reportprojectVersionSelect.setLabel("Version");
		propertiesLayout.add(reportprojectVersionSelect);

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

	public Select<ProjectVersion> getReportprojectVersionSelect() {
		return reportprojectVersionSelect;
	}

	public void setReportprojectVersionSelect(Select<ProjectVersion> reportprojectVersionSelect) {
		this.reportprojectVersionSelect = reportprojectVersionSelect;
	}

}
