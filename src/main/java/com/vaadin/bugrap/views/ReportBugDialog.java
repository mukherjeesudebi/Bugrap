package com.vaadin.bugrap.views;

import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;

import com.vaadin.bugrap.components.PriorityInput;
import com.vaadin.bugrap.service.ProjectVersionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

public class ReportBugDialog extends Dialog {
    private Project selectedProject;
    private ProjectVersionService projectVersionService;
    private Select<ProjectVersion> projectVersionSelect;

    public ReportBugDialog(Project project,
            ProjectVersionService projectVersionService) {
        this.selectedProject = project;
        this.projectVersionSelect = new Select<ProjectVersion>();
        this.projectVersionService = projectVersionService;
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
        Span projectVersionLabel = new Span();
        projectVersionLabel.setText("Project Version :");
        projectVersionDiv.add(projectVersionLabel,this.projectVersionSelect);
        add(projectVersionDiv);
        //add(new PriorityInput());
        setModal(true);
        setHeaderTitle(
                "Report a Bug for Project: " + selectedProject.getName());
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> this.close());
        closeButton.addThemeName("bugrap-button-link");
        getHeader().add(closeButton);
    }

    private List<ProjectVersion> addValuesToProjectVersionsSelect() {
        return projectVersionService.getAllProjectVersions(selectedProject);
    }
}
