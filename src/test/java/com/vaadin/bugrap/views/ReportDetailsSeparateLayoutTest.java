package com.vaadin.bugrap.views;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.bugrap.TestViewSecurityConfig;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridTester;
import com.vaadin.flow.component.select.Select;
import com.vaadin.testbench.unit.SpringUIUnit4Test;

@ContextConfiguration(classes = TestViewSecurityConfig.class)
public class ReportDetailsSeparateLayoutTest extends SpringUIUnit4Test {

    @Test
    @WithMockUser(username = "admin", password="admin")
    public void createReportsView() {
        navigate(MainLayout.class);
        Grid<Report> grid = $(Grid.class).first();
        GridTester gridTester = test(grid);

        gridTester.select(0);
        gridTester.select(1);

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
        assertEquals("BUG",gridTester.getCellText(0, 2));
        assertEquals("developer",gridTester.getCellText(0, 4));
        
        assertEquals("Version 2",gridTester.getCellText(1, 0));
        assertEquals("BUG",gridTester.getCellText(1, 2));
        assertEquals("developer",gridTester.getCellText(1, 4));

    }
}
