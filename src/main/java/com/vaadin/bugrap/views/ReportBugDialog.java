package com.vaadin.bugrap.views;

import java.util.Date;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;

import com.vaadin.bugrap.dao.ReportDao;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.bugrap.service.ReportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ReportBugDialog extends Dialog {
    private Project selectedProject;
    private ProjectVersionService projectVersionService;
    private Select<ProjectVersion> projectVersionSelect;
    private Div priorityIndicatior;
    private ReportService reportService;
    private Report.Type type;
    private Button reportBugFeatureButton;

    public ReportBugDialog(Project project,
            ProjectVersionService projectVersionService,
            ReportService reportService, Report.Type type) {
        this.selectedProject = project;
        this.projectVersionSelect = new Select<ProjectVersion>();
        this.projectVersionService = projectVersionService;
        this.reportService = reportService;
        this.type = type;
        this.addDialogContents();
    }

    public void addDialogContents() {

        List<ProjectVersion> projectVersionsList = this
                .addValuesToProjectVersionsSelect();
        this.projectVersionSelect.setItems(projectVersionsList);
        this.projectVersionSelect.setValue(projectVersionsList.get(0));
        this.createDialogView();
    }

    private void createDialogView() {
        VerticalLayout dialogMainLayout = new VerticalLayout();
        Div projectVersionDiv = new Div();
        Div projectVersionLabel = new Div();
        projectVersionLabel.setText("Project Version : ");
        projectVersionLabel.setWidth("22%");
        projectVersionSelect.addThemeName("bugrap-report-select");
        projectVersionDiv.add(projectVersionLabel, this.projectVersionSelect);
        projectVersionDiv.setWidthFull();
        projectVersionDiv.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.START,
                LumoUtility.AlignItems.CENTER);
        add(projectVersionDiv);

        Div priorityContinerDiv = new Div();
        Div priorityLabel = new Div();
        priorityLabel.setText("Priority : ");
        priorityLabel.setWidth("22%");
        Div priorityRadioBox = new Div();
        RadioButtonGroup<Report.Priority> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems(Report.Priority.TRIVIAL, Report.Priority.MINOR,
                Report.Priority.NORMAL, Report.Priority.MAJOR,
                Report.Priority.CRITICAL, Report.Priority.BLOCKER);
        radioGroup.setValue(Report.Priority.TRIVIAL);
        priorityIndicatior = new Div();
        priorityIndicatior.setWidthFull();
        priorityIndicatior.setHeight("10px");
        priorityIndicatior.getStyle().set("background-image",
                "linear-gradient(to right, #b4eea0, #44c119)");
        priorityRadioBox.add(radioGroup, priorityIndicatior);

        priorityContinerDiv.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.START,
                LumoUtility.AlignItems.CENTER);
        priorityContinerDiv.add(priorityLabel, priorityRadioBox);
        priorityContinerDiv.addClassName(LumoUtility.Padding.Bottom.SMALL);
        add(priorityContinerDiv);

        Div summaryDiv = new Div();
        Div summaryLabel = new Div();
        summaryLabel.setText("Summary : ");
        summaryLabel.setWidth("22%");
        TextField summaryTextField = new TextField();
        summaryTextField.setMaxLength(500);
        summaryTextField.setWidth("75%");
        summaryDiv.add(summaryLabel, summaryTextField);
        summaryDiv.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.START,
                LumoUtility.AlignItems.CENTER);
        summaryDiv.addClassName(LumoUtility.Padding.Bottom.XSMALL);
        add(summaryDiv);

        Div descriptionDiv = new Div();
        Div descriptionLabel = new Div();
        descriptionLabel.setText("Description : ");
        descriptionLabel.setWidth("22%");
        RichTextEditor descriptionTextArea = new RichTextEditor();
        descriptionTextArea.setWidth("75%");
        descriptionDiv.add(descriptionLabel, descriptionTextArea);
        descriptionDiv.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.START,
                LumoUtility.AlignItems.CENTER);
        add(descriptionDiv);

        if (type == Report.Type.BUG) {
            reportBugFeatureButton = new Button("Report Bug",
                    new Icon(VaadinIcon.BUG));

        } else {
            reportBugFeatureButton = new Button("Request Feature",
                    new Icon(VaadinIcon.LIGHTBULB));
        }

        reportBugFeatureButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getFooter().add(reportBugFeatureButton);

        setModal(true);
        if (type == Report.Type.BUG) {
            setHeaderTitle(
                    "Report a bug for Project: " + selectedProject.getName());
        } else {
            setHeaderTitle("Request a feature for Project: "
                    + selectedProject.getName());
        }

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> this.close());
        closeButton.addThemeName("bugrap--modal-close-link");
        getHeader().add(closeButton);
        addThemeName("vaadin-modal");
        setWidth("60%");

        radioGroup.addValueChangeListener(event -> {
            Report.Priority selectedPriority = event.getValue();
            if (Report.Priority.TRIVIAL == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #b4eea0, #44c119)");
            } else if (Report.Priority.MINOR == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #b4eea0, #44c119,#ffba00, #ffcf4d)");
            } else if (Report.Priority.NORMAL == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #b4eea0,#fbd85f,#ff8100)");
            } else if (Report.Priority.MAJOR == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #b4eea0,#fbd85f,#ff8100,#ff8100)");
            } else if (Report.Priority.CRITICAL == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #b4eea0,#fbd85f,#ff8100,#ff8100, #ff0000)");
            } else if (Report.Priority.BLOCKER == selectedPriority) {
                priorityIndicatior.getStyle().set("background-image",
                        "linear-gradient(to right, #ff9d38,#ff8100, #ff0000)");
            }
        });

        reportBugFeatureButton.addClickListener(event -> {
            Report report = new Report();
            report.setProject(selectedProject);
            report.setVersion(projectVersionSelect.getValue());
            report.setStatus(Report.Status.OPEN);
            report.setPriority(radioGroup.getValue());
            report.setSummary(summaryTextField.getValue());
            report.setDescription(descriptionTextArea.getHtmlValue());
            report.setReportedTimestamp(new Date());
            report.setType(type);

            reportService.saveUpdatedReportDetails(report);
        });
    }

    private List<ProjectVersion> addValuesToProjectVersionsSelect() {
        return projectVersionService.getAllProjectVersions(selectedProject);
    }
}
