package com.vaadin.bugrap.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.dao.CommentDao;
import com.vaadin.bugrap.dao.ReporterDao;
import com.vaadin.bugrap.security.AuthenticatedUser;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ReportDetailsLayout extends VerticalLayout {

    private Grid<Report> grid;
    private GridDataView<Report> gridDataView;
    private Binder<Report> reportBinder;
    private Div reportSummary;
    private Select<Priority> prioritySelect;
    private Select<Type> typeSelect;
    private Select<Status> statusSelect;
    private Select<Reporter> assignedToSelect;
    private Select<ProjectVersion> reportprojectVersionSelect;

    private ReporterDao reporterDao;
    private Project selectedProject;

    private ProjectVersionService projectVersionService;
    private ReportService reportService;
    private VerticalLayout reportCommentsLayout = new VerticalLayout();
    private VerticalLayout commentLayout = new VerticalLayout();

    private Icon revertIcon = new Icon("lumo", "reload");
    private Button saveChangesButton = new Button("Save Changes");
    private Button revertChangesButton = new Button("Revert", revertIcon);
    private Report selectedReport;
    private Button openButton;
    private Set<Report> selectedReports;
    private CommentDao commentDao;
    private RichTextEditor richTextEditor;
    private AuthenticatedUser authenticatedUserImpl;

    public ReportDetailsLayout(ReporterDao reporterDao,
            ProjectVersionService projectVersionService,
            ReportService reportService, CommentDao commentDao,AuthenticatedUser authenticatedUserImpl) {
        this.reporterDao = reporterDao;
        this.projectVersionService = projectVersionService;
        this.reportService = reportService;
        reportBinder = new Binder<>(Report.class);
        this.commentDao = commentDao;
        this.authenticatedUserImpl = authenticatedUserImpl;
    }

    public void connectGrid() {
        gridDataView = grid.getGenericDataView();
        grid.asMultiSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                selectedReports = event.getValue();
                if (selectedReports.size() == 1) {
                    selectedReport = selectedReports.stream().toList().get(0);
                    reportSummary.setText(selectedReport.getSummary());
                    reportBinder.bind(prioritySelect, Report::getPriority,
                            Report::setPriority);
                    reportBinder.bind(typeSelect, Report::getType,
                            Report::setType);
                    reportBinder.bind(statusSelect, Report::getStatus,
                            Report::setStatus);
                    reportBinder.bind(assignedToSelect, Report::getAssigned,
                            Report::setAssigned);
                    reportBinder.bind(reportprojectVersionSelect,
                            Report::getVersion, Report::setVersion);
                    reportBinder.readBean(selectedReport);
                    openButton.setVisible(true);
                    addExistingComments();
                    createCommentsAttachmentsView();
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
                Notification successNotification = Notification.show(
                        "Report Details Saved Successfully", 3000,
                        Position.MIDDLE);
                successNotification
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                reportBinder.writeBean(new Report());
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
        reportSummary = new Div();
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
        prioritySelect.setId("prioritySelect");
        propertiesLayout.add(prioritySelect);

        typeSelect = new Select<Type>();
        typeSelect.setItems(Stream.of(Report.Type.values()).toList());
        typeSelect.setLabel("Type");
        typeSelect.addThemeName("bugrap-report-select");
        typeSelect.setId("typeSelect");
        propertiesLayout.add(typeSelect);

        statusSelect = new Select<Status>();
        statusSelect.setItems(Stream.of(Report.Status.values()).toList());
        statusSelect.setLabel("Status");
        statusSelect.addThemeName("bugrap-report-select");
        statusSelect.setId("statusSelect");
        propertiesLayout.add(statusSelect);

        assignedToSelect = new Select<Reporter>();
        assignedToSelect.setItems(this.reporterDao.getAllReporters());
        assignedToSelect.setLabel("Assigned to");
        assignedToSelect.addThemeName("bugrap-report-select");
        assignedToSelect.setId("assignedToSelect");
        propertiesLayout.add(assignedToSelect);

        reportprojectVersionSelect = new Select<ProjectVersion>();
        reportprojectVersionSelect.setItems(this.projectVersionService
                .getAllProjectVersions(this.selectedProject));
        reportprojectVersionSelect.setLabel("Version");
        reportprojectVersionSelect.addThemeName("bugrap-report-select");
        reportprojectVersionSelect.setId("reportprojectVersionSelect");
        propertiesLayout.add(reportprojectVersionSelect);

        saveChangesButton.setId("saveChangesButton");
        propertiesActionlLayout.add(saveChangesButton);
        saveChangesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        propertiesActionlLayout.add(revertChangesButton);
        revertChangesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        reportPropertieAndAction.add(propertiesLayout);
        reportPropertieAndAction.add(propertiesActionlLayout);
        reportPropertieAndAction.setAlignItems(Alignment.BASELINE);
        reportPropertieAndAction
                .setJustifyContentMode(JustifyContentMode.BETWEEN);
        reportPropertieAndAction.setWidthFull();
        add(reportPropertieAndAction);
        reportCommentsLayout.addClassName(LumoUtility.Padding.NONE);
        add(reportCommentsLayout);

    }

    public void addExistingComments() {
        Map<Reporter, List<Comment>> commentsMap = commentDao
                .getAllComments(selectedReport);

        if (commentsMap != null) {
            this.reportCommentsLayout.remove(commentLayout);
            commentLayout = new VerticalLayout();
            commentLayout.addClassName(LumoUtility.Padding.NONE);;

            for (Map.Entry<Reporter, List<Comment>> comments : commentsMap
                    .entrySet()) {
                Map<Date, List<Comment>> separatedComments = comments.getValue()
                        .stream()
                        .collect(Collectors.groupingBy(Comment::getTimestamp));
                for (Map.Entry<Date, List<Comment>> singleComment : separatedComments
                        .entrySet()) {
                    HorizontalLayout singleCommentLayout = new HorizontalLayout();
                    singleCommentLayout.setWidthFull();
                    singleCommentLayout
                            .setJustifyContentMode(JustifyContentMode.BETWEEN);

                    Div commentString = new Div();
                    commentString.setWidth("75%");
                    commentString.setClassName(LumoUtility.Padding.MEDIUM);
                    singleCommentLayout.add(commentString);

                    VerticalLayout userAttachmentsLayout = new VerticalLayout();
                    Comment userDetailsAndTime = singleComment.getValue()
                            .get(0);

                    Div userDiv = new Div();
                    userDiv.addClassName(LumoUtility.Display.FLEX);
                    userDiv.addClassName(LumoUtility.FlexDirection.ROW);
                    Avatar userAvatar = new Avatar(userDetailsAndTime
                            .getAuthor().getName().toUpperCase());
                    userAvatar.getStyle().set("background-color", "#BC8341");
                    userAvatar.getStyle().set("color", "white");
                    userDiv.add(userAvatar);
                    Div userNameTimeDiv = new Div();
                    Div userNameDiv = new Div();
                    userNameDiv
                            .setText(userDetailsAndTime.getAuthor().getName());
                    userNameDiv.addClassName(LumoUtility.FontWeight.BOLD);
                    Div timeDiv = new Div();
                    timeDiv.setText(getDuration(
                            singleComment.getValue().get(0).getTimestamp()));
                    userNameTimeDiv.add(userNameDiv, timeDiv);
                    userNameTimeDiv.addClassName(LumoUtility.Display.FLEX);
                    userNameTimeDiv
                            .addClassName(LumoUtility.FlexDirection.COLUMN);
                    userNameTimeDiv
                            .addClassName(LumoUtility.Padding.Left.MEDIUM);
                    userDiv.add(userNameTimeDiv);

                    userAttachmentsLayout.add(userDiv);
                    userAttachmentsLayout.setWidth("25%");

                    singleCommentLayout.add(userAttachmentsLayout);
                    singleCommentLayout.setWidthFull();
                    singleCommentLayout.addClassName(LumoUtility.Border.ALL);
                    singleCommentLayout.addClassName("commentLayout");

                    for (Comment comment : singleComment.getValue()) {
                        if (comment.getType() == Comment.Type.COMMENT) {
                            // commentString.setText(comment.getComment());
                            String htmlText = "<div>" + comment.getComment()
                                    + "</div>";
                            Html htmlComponent = new Html(htmlText);
                            commentString.add(htmlComponent);

                        } else if (comment
                                .getType() == Comment.Type.ATTACHMENT) {
                            Div attachmentDiv = new Div();
                            // attachment.setText(comment.getAttachmentName());
                            addLinkToFile(comment.getAttachmentName(),
                                    comment.getAttachment(), attachmentDiv);
                            userAttachmentsLayout.add(attachmentDiv);
                        }
                    }
                    commentLayout.add(singleCommentLayout);

                }

            }
            this.reportCommentsLayout.add(commentLayout);
        }

    }

    private void addLinkToFile(String fileName, byte[] attachment,
            Div attachmentDiv) {
        StreamResource streamResource = new StreamResource(fileName, () -> getStream(attachment));
        Anchor link = new Anchor(streamResource, fileName);
        link.getElement().setAttribute("download", true);
        attachmentDiv.add(link);
    }
    
    private InputStream getStream(byte[] attachment) {
        return new ByteArrayInputStream(attachment);
    }

    private String getDuration(Date timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        StringBuilder str = new StringBuilder();
        try {
            Date d1 = sdf.parse(sdf.format(new Date()));
            Date d2 = sdf.parse(sdf.format(timestamp));
            long difference_In_Time = d1.getTime() - d2.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60))
                    % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days = (difference_In_Time
                    / (1000 * 60 * 60 * 24)) % 365;

            if (difference_In_Years > 0) {
                str.append(difference_In_Years + " years ");
            } else if (difference_In_Days > 0) {
                str.append(difference_In_Days + " days ");
            } else if (difference_In_Hours > 0) {
                str.append(difference_In_Hours + " hours ");
            } else if (difference_In_Minutes > 0) {
                str.append(difference_In_Minutes + " minutes ");
            } else if (difference_In_Seconds > 0) {
                str.append(difference_In_Seconds + " seconds ");
            }
            if (!str.isEmpty()) {
                str.append("ago");
            } else {
                str.append("just now");
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str.toString();
    }

    
    public void createCommentsAttachmentsView() {
        HorizontalLayout commentsAttachmentsLayout = new HorizontalLayout();

        VerticalLayout editorLayout = new VerticalLayout();
        // RichTextEditor richTextEditor = new RichTextEditor();
        richTextEditor = new RichTextEditor();
        richTextEditor.setHeight("250px");
        richTextEditor.setWidth("800px");
        richTextEditor.addThemeVariants(RichTextEditorVariant.LUMO_COMPACT);
        // richTextEditor.addClassName("richTextEditor");
        editorLayout.add(richTextEditor);

        HorizontalLayout commentButtonsDiv = new HorizontalLayout();
        Icon checkmarkLumoIcon = new Icon("lumo", "checkmark");
        Button addCommentButton = new Button("Comment", checkmarkLumoIcon);
        addCommentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Icon crossLumoIcon = new Icon("lumo", "cross");
        Button cancelButton = new Button("Cancel", crossLumoIcon);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        commentButtonsDiv.add(addCommentButton, cancelButton);
        editorLayout.add(commentButtonsDiv);

        commentsAttachmentsLayout.add(editorLayout);

        VerticalLayout attachmentsLayout = new VerticalLayout();

        Div helperText = new Div();
        Div helperTextHeader = new Div();
        helperTextHeader.setText("Attachments");
        Div helperTextBody = new Div();
        helperTextBody.setText(
                "Only PDF, PNG and JPG files are allowed. Max file size is 5MB.");
        helperText.add(helperTextHeader, helperTextBody);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf", ".pdf", "image/jpeg",
                ".jpeg", "jpg", "image/png", ".png");
        int maxFileSizeInBytes = 5 * 1024 * 1024;
        upload.setMaxFileSize(maxFileSizeInBytes);

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        attachmentsLayout.add(helperText);
        attachmentsLayout.add(upload);
        attachmentsLayout.setWidth("30%");

        /*
         * upload.addSucceededListener(event -> { String fileName =
         * event.getFileName(); InputStream inputStream =
         * buffer.getInputStream(fileName);
         * createNewAttachment(fileName,inputStream,report);
         * 
         * // Do something with the file data // processFile(inputStream,
         * fileName); });
         * 
         * 
         */
        commentsAttachmentsLayout.add(attachmentsLayout);
        commentsAttachmentsLayout.setWidthFull();
        commentsAttachmentsLayout
                .setJustifyContentMode(JustifyContentMode.BETWEEN);
        commentsAttachmentsLayout.getStyle().set("background-color",
                "#d3d3d3a3");

        addCommentButton.addClickListener(event -> {
            createNewComment(buffer);
            this.addExistingComments();
            richTextEditor.asDelta().setValue("");
        });
        add(commentsAttachmentsLayout);

    }
    
    public void createNewComment(MultiFileMemoryBuffer buffer) {
        Set<String> files = buffer.getFiles();
        Date timestamp = new Date();

        for (String fileName : files) {
            Comment attachment = new Comment();
            attachment.setType(Comment.Type.ATTACHMENT);
            attachment.setAttachmentName(fileName);
            attachment.setReport(selectedReport);
            attachment.setTimestamp(timestamp);
            attachment.setAuthor(authenticatedUserImpl.get().get());
            try {
                attachment.setAttachment(
                        buffer.getInputStream(fileName).readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            commentDao.saveComment(attachment);

        }
        if (!richTextEditor.asDelta().isEmpty()) {
            Comment comment = new Comment();
            richTextEditor.getHtmlValue();
            comment.setType(Comment.Type.COMMENT);
            comment.setReport(selectedReport);
            comment.setTimestamp(timestamp);
            comment.setComment(richTextEditor.getHtmlValue());
            comment.setAuthor(authenticatedUserImpl.get().get());
            commentDao.saveComment(comment);
        }

        Notification successNotification = Notification
                .show("Comment Saved Successfully", 3000, Position.MIDDLE);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

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

    public void setReportprojectVersionSelect(
            Select<ProjectVersion> reportprojectVersionSelect) {
        this.reportprojectVersionSelect = reportprojectVersionSelect;
    }

}
