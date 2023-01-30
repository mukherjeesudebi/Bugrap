package com.vaadin.bugrap.views;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.CommentDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.richtexteditor.RichTextEditorVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("reportDetails")
public class ReportDetailsSeparateLayout extends VerticalLayout
		implements HasUrlParameter<Long>, AfterNavigationObserver {
	private Report report;
	private Long reportId;
	private ReporterDao reporterDao;
	private ProjectVersionService projectVersionService;
	private Binder<Report> reportBinder;
	private ReportService reportService;
	private CommentDao commentDao;

	public ReportDetailsSeparateLayout(ReporterDao reporterDao, ProjectVersionService projectVersionService,
			ReportService reportService,CommentDao commentDao) {
		this.reporterDao = reporterDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		reportBinder = new Binder<>(Report.class);
		this.commentDao = commentDao;
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		reportId = parameter;

	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		report = this.reportService.findReportById(reportId).get();
		createSingleReportView();
		createCommentsAttachmentsView();
		setHeightFull();
		setJustifyContentMode(JustifyContentMode.BETWEEN);
	}

	public void createSingleReportView() {
		VerticalLayout reportDetailsLayout = new VerticalLayout();
		HorizontalLayout projectNameAndVersion = new HorizontalLayout();

		Div projectName = new Div();
		projectName.setText(report.getProject().getName());
		Div projectVersion = new Div();
		projectVersion.setText(report.getVersion() != null ? report.getVersion().getVersion() : "");
		projectNameAndVersion.add(projectName, projectVersion);
		reportDetailsLayout.add(projectNameAndVersion);

		H6 reportSummary = new H6();
		reportSummary.setText(report.getSummary());
		reportDetailsLayout.add(reportSummary);

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
		reportprojectVersionSelect.setItems(this.projectVersionService.getAllProjectVersions(this.report.getProject()));
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
		reportDetailsLayout.add(reportPropertieAndAction);

		/*
		 * Div descriptionDiv = new Div();
		 * descriptionDiv.setText(report.getDescription()); add(descriptionDiv);
		 */

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

		add(reportDetailsLayout);

	}

	public void createCommentsAttachmentsView() {
		HorizontalLayout commentsAttachmentsLayout = new HorizontalLayout();

		VerticalLayout editorLayout = new VerticalLayout();
		RichTextEditor richTextEditor = new RichTextEditor();
		richTextEditor.setMaxHeight("400px");
		richTextEditor.setMinHeight("200px");
		richTextEditor.addThemeVariants(RichTextEditorVariant.LUMO_COMPACT);
		editorLayout.add(richTextEditor);

		HorizontalLayout commentButtonsDiv = new HorizontalLayout();
		Icon checkmarkLumoIcon = new Icon("lumo", "checkmark");
		Button addCommentButton = new Button("Comment", checkmarkLumoIcon);
		Icon crossLumoIcon = new Icon("lumo", "cross");
		Button cancelButton = new Button("Cancel", crossLumoIcon);
		commentButtonsDiv.add(addCommentButton, cancelButton);
		editorLayout.add(commentButtonsDiv);

		commentsAttachmentsLayout.add(editorLayout);

		MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
		Upload upload = new Upload(buffer);

		/*
		 * upload.addSucceededListener(event -> { String fileName = event.getFileName();
		 * InputStream inputStream = buffer.getInputStream(fileName);
		 * createNewAttachment(fileName,inputStream,report);
		 * 
		 * // Do something with the file data // processFile(inputStream, fileName); });
		 */
		commentsAttachmentsLayout.add(upload);
		commentsAttachmentsLayout.setWidthFull();
		commentsAttachmentsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		commentsAttachmentsLayout.getStyle().set("background-color", "#f0f0f0");
		
		addCommentButton.addClickListener(event -> {
			createNewComment(buffer);
			showAllComments();
		});
		add(commentsAttachmentsLayout);

	}
	
	public void createNewComment(MultiFileMemoryBuffer buffer) {
		Set<String> files = buffer.getFiles();
		Date timestamp = new Date();
		
		for(String fileName : files) {
			Comment attachment = new Comment();
			attachment.setType(Comment.Type.ATTACHMENT);
			attachment.setAttachmentName(fileName);
			attachment.setReport(report);
			attachment.setTimestamp(timestamp);
			try {
				attachment.setAttachment(buffer.getInputStream(fileName).readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			commentDao.saveComment(attachment);
			
		}
		
		Comment comment = new Comment();
		comment.setType(Comment.Type.COMMENT);
		comment.setReport(report);
		comment.setTimestamp(timestamp);
		commentDao.saveComment(comment);
		
		
		
	}
	
	public void showAllComments() {
		List<Comment> comments = commentDao.getAllComments(report);
		
	}

}
