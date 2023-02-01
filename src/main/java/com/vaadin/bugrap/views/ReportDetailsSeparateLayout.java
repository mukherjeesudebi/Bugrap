package com.vaadin.bugrap.views;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.CommentDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.SecurityService;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@PermitAll
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
	private TextArea richTextEditor;
	private SecurityService securityService;

	public ReportDetailsSeparateLayout(ReporterDao reporterDao, ProjectVersionService projectVersionService,
			ReportService reportService, CommentDao commentDao, SecurityService securityService) {
		this.reporterDao = reporterDao;
		this.projectVersionService = projectVersionService;
		this.reportService = reportService;
		reportBinder = new Binder<>(Report.class);
		this.commentDao = commentDao;
		this.securityService = securityService;
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

		addExistingComments(reportDetailsLayout);

		add(reportDetailsLayout);

	}

	public void createCommentsAttachmentsView() {
		HorizontalLayout commentsAttachmentsLayout = new HorizontalLayout();

		VerticalLayout editorLayout = new VerticalLayout();
		// RichTextEditor richTextEditor = new RichTextEditor();
		richTextEditor = new TextArea();
		richTextEditor.setHeight("250px");
		richTextEditor.setWidth("800px");
		// richTextEditor.addThemeVariants(RichTextEditorVariant.LUMO_COMPACT);
		editorLayout.add(richTextEditor);

		HorizontalLayout commentButtonsDiv = new HorizontalLayout();
		Icon checkmarkLumoIcon = new Icon("lumo", "checkmark");
		Button addCommentButton = new Button("Comment", checkmarkLumoIcon);
		Icon crossLumoIcon = new Icon("lumo", "cross");
		Button cancelButton = new Button("Cancel", crossLumoIcon);
		commentButtonsDiv.add(addCommentButton, cancelButton);
		editorLayout.add(commentButtonsDiv);

		commentsAttachmentsLayout.add(editorLayout);

		VerticalLayout attachmentsLayout = new VerticalLayout();

		Div helperText = new Div();
		Div helperTextHeader = new Div();
		helperTextHeader.setText("Attachments");
		Div helperTextBody = new Div();
		helperTextBody.setText("Only PDF, PNG and JPG files are allowed. Max file size is 5MB.");
		helperText.add(helperTextHeader, helperTextBody);

		MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("application/pdf", ".pdf", "image/jpeg", ".jpeg", "jpg", "image/png", ".png");
		int maxFileSizeInBytes = 5 * 1024 * 1024;
		upload.setMaxFileSize(maxFileSizeInBytes);

		upload.addFileRejectedListener(event -> {
			String errorMessage = event.getErrorMessage();

			Notification notification = Notification.show(errorMessage, 5000, Notification.Position.MIDDLE);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
		});

		attachmentsLayout.add(helperText);
		attachmentsLayout.add(upload);
		attachmentsLayout.setWidth("26%");

		/*
		 * upload.addSucceededListener(event -> { String fileName = event.getFileName();
		 * InputStream inputStream = buffer.getInputStream(fileName);
		 * createNewAttachment(fileName,inputStream,report);
		 * 
		 * // Do something with the file data // processFile(inputStream, fileName); });
		 * 
		 * 
		 */
		commentsAttachmentsLayout.add(attachmentsLayout);
		commentsAttachmentsLayout.setWidthFull();
		commentsAttachmentsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		commentsAttachmentsLayout.getStyle().set("background-color", "#f0f0f0");

		addCommentButton.addClickListener(event -> {
			createNewComment(buffer);
			showAllComments();
		});
		add(commentsAttachmentsLayout);

	}

	public void addExistingComments(VerticalLayout reportDetailsLayout) {
		List<Comment> comments = commentDao.getAllComments(report);
		Div commentDiv = new Div();

		for (Comment comment : comments) {
			if (comment.getType() == Comment.Type.COMMENT) {
				Div commentString = new Div();
				commentString.setText(comment.getComment());
				commentDiv.add(commentString);
			} else if (comment.getType() == Comment.Type.ATTACHMENT) {
				Div attachmentDiv = new Div();
				//attachment.setText(comment.getAttachmentName());
				addLinkToFile(comment.getAttachmentName(), comment.getAttachment(),attachmentDiv);
				commentDiv.add(attachmentDiv);
			}
		}

		reportDetailsLayout.add(commentDiv);
	}

	private void addLinkToFile(String fileName,byte[] attachment,Div attachmentDiv) {
        StreamResource streamResource = new StreamResource(fileName, ()-> getStream(attachment));
        Anchor link = new Anchor(streamResource, fileName);
        link.getElement().setAttribute("download", true);
        attachmentDiv.add(link);
    }

	private InputStream getStream(byte[] attachment) {
		return new ByteArrayInputStream(attachment);
	}

	public void createNewComment(MultiFileMemoryBuffer buffer) {
		Set<String> files = buffer.getFiles();
		Date timestamp = new Date();

		for (String fileName : files) {
			Comment attachment = new Comment();
			attachment.setType(Comment.Type.ATTACHMENT);
			attachment.setAttachmentName(fileName);
			attachment.setReport(report);
			attachment.setTimestamp(timestamp);
			attachment.setAuthor(securityService.getAuthenticatedUser());
			try {
				attachment.setAttachment(buffer.getInputStream(fileName).readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			commentDao.saveComment(attachment);

		}
		if (richTextEditor.getValue() != null || !("").equals(richTextEditor.getValue())) {
			Comment comment = new Comment();
			comment.setType(Comment.Type.COMMENT);
			comment.setReport(report);
			comment.setTimestamp(timestamp);
			comment.setComment(richTextEditor.getValue());
			comment.setAuthor(securityService.getAuthenticatedUser());
			commentDao.saveComment(comment);
		}

	}

	public void showAllComments() {
		List<Comment> comments = commentDao.getAllComments(report);

	}

}
