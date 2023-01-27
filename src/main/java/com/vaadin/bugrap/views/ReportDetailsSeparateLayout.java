package com.vaadin.bugrap.views;

import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route("reportDetails")
public class ReportDetailsSeparateLayout extends VerticalLayout{
	private Report report;
	private ReporterDao reporterDao;
	private ProjectVersionService projectVersionService;
	private Binder<Report> reportBinder;
	private ReportService reportService;

	public ReportDetailsSeparateLayout(ReporterDao reporterDao,ProjectVersionService projectVersionService,ReportService reportService) {
		this.reporterDao = reporterDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		reportBinder = new Binder<>(Report.class);
		report = (Report)VaadinSession.getCurrent().getAttribute("Report");
		createSingleReportView();
	}
	


	public void createSingleReportView() {

		HorizontalLayout projectNameAndVersion = new HorizontalLayout();
		
		Div projectName = new Div();
		projectName.setText(report.getProject().getName());
		Div projectVersion = new Div();
		projectVersion.setText(report.getVersion().getVersion());
		projectNameAndVersion.add(projectName,projectVersion);
		add(projectNameAndVersion);
		
		H6 reportSummary = new H6();
		reportSummary.setText(report.getSummary());
		add(reportSummary);

		HorizontalLayout reportPropertieAndAction = new HorizontalLayout();
		HorizontalLayout propertiesLayout = new HorizontalLayout();
		HorizontalLayout propertiesActionlLayout = new HorizontalLayout();

		Select<Priority> prioritySelect = new Select<Priority>();
		prioritySelect.setItems(Stream.of(Report.Priority.values()).toList());
		prioritySelect.setLabel("Priority");
		propertiesLayout.add(prioritySelect);

		Select<Type> typeSelect = new Select<Type>();
		typeSelect.setItems(Stream.of(Report.Type.values()).toList());
		typeSelect.setLabel("Type");
		propertiesLayout.add(typeSelect);

		Select<Status> statusSelect = new Select<Status>();
		statusSelect.setItems(Stream.of(Report.Status.values()).toList());
		statusSelect.setLabel("Status");
		propertiesLayout.add(statusSelect);

		Select<Reporter> assignedToSelect = new Select<Reporter>();
		assignedToSelect.setItems(this.reporterDao.getAllReporters());
		assignedToSelect.setLabel("Assigned to");
		propertiesLayout.add(assignedToSelect);

		Select<ProjectVersion> reportprojectVersionSelect = new Select<ProjectVersion>();
		reportprojectVersionSelect.setItems(this.projectVersionService.getAllProjectVersions(this.report.getVersion().getProject()));
		reportprojectVersionSelect.setLabel("Version");
		propertiesLayout.add(reportprojectVersionSelect);
		
		Button saveChangesButton = new Button("Save changes");
		Button revertChangesButton = new Button("Revert");

		propertiesActionlLayout.add(saveChangesButton);
		propertiesActionlLayout.add(revertChangesButton);

		reportPropertieAndAction.add(propertiesLayout);
		reportPropertieAndAction.add(propertiesActionlLayout);
		reportPropertieAndAction.setAlignItems(Alignment.BASELINE);
		reportPropertieAndAction.setJustifyContentMode(JustifyContentMode.BETWEEN);
		reportPropertieAndAction.setWidthFull();
		add(reportPropertieAndAction);

		Div descriptionDiv = new Div();
		descriptionDiv.setText(report.getDescription());
		add(descriptionDiv);
		
		reportBinder.bind(prioritySelect, Report::getPriority, Report::setPriority);
		reportBinder.bind(typeSelect, Report::getType, Report::setType);
		reportBinder.bind(statusSelect, Report::getStatus, Report::setStatus);
		reportBinder.bind(assignedToSelect, Report::getAssigned, Report::setAssigned);
		reportBinder.bind(reportprojectVersionSelect, Report::getVersion, Report::setVersion);
		reportBinder.readBean(report);
		
		saveChangesButton.addClickListener(event -> {
			try {
				reportBinder.writeBean(report);
				this.reportService.saveUpdatedReportDetails(report);
				Notification successNotification = Notification.show("Report Details Saved Successfully", 3000,
						Position.MIDDLE);
				successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		});
		revertChangesButton.addClickListener(event -> {
			reportBinder.readBean(report);
		});

	}

}
