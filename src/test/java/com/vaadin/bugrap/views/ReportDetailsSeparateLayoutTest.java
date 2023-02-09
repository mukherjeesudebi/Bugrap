package com.vaadin.bugrap.views;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.TestViewSecurityConfig;
import com.vaadin.bugrap.security.MockSecurityServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridTester;
import com.vaadin.flow.component.select.Select;
import com.vaadin.testbench.unit.SpringUIUnit4Test;

@ContextConfiguration(classes = TestViewSecurityConfig.class)
public class ReportDetailsSeparateLayoutTest extends SpringUIUnit4Test {

    @Test
    public void createReportsView() {
        Reporter admin = new Reporter();
        admin.setName("admin");
        admin.setEmail("admin@bugrap.com");
        admin.setConsistencyVersion(0);
        admin.setAdmin(false);
        admin.setId(1);

        ProjectVersion projectVersion2 = new ProjectVersion();
        projectVersion2.setVersion("Version 2");
        projectVersion2.setClosed(false);
        projectVersion2.setId(6);
        projectVersion2.setConsistencyVersion(0);
        projectVersion2.setReleaseDate(null);

        navigate(MainLayout.class);
        Grid<Report> grid = $(Grid.class).first();
        GridTester gridTester = test(grid);

        gridTester.select(0);
        gridTester.select(0);

        test($(Select.class).withId("prioritySelect").first())
                .selectItem("MAJOR");

        test($(Select.class).withId("typeSelect").first()).selectItem("BUG");
        test($(Select.class).withId("statusSelect").first())
                .selectItem("Fixed");
        test($(Select.class).withId("assignedToSelect").first())
                .selectItem("developer");
        test($(Select.class).withId("reportprojectVersionSelect").first())
                .selectItem("Version 2");

        test($(Button.class).withId("saveChangesButton").first()).click();

        

        assertEquals("Version 2",gridTester.getCellText(0, 0));
        assertEquals("MAJOR",gridTester.getCellComponent(0, 1).getElement().getProperty("priority"));
        assertEquals("BUG",gridTester.getCellText(0, 2));
        assertEquals("developer",gridTester.getCellText(0, 4));

    }
}
