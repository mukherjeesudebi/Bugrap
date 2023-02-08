package com.vaadin.bugrap.integrationtest;

import org.junit.Test;
import org.openqa.selenium.NotFoundException;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.select.testbench.SelectElement;

public class MainLayoutIT extends BugrapITTest {
    @Override
    public void setup() throws Exception {
        super.setup();
        // Hide dev mode gizmo, it would interfere screenshot tests
        try {
            $("vaadin-dev-tools").first().setProperty("hidden", true);
        } catch (NotFoundException e) {

        }

        login("admin", "admin");
    }

    @Test
    public void updateReportTest() throws InterruptedException {
        GridElement grid = $(GridElement.class).first();
        grid.select(0);
        grid.select(1);

        $(SelectElement.class).id("prioritySelect").selectByText("MAJOR");
        $(SelectElement.class).id("typeSelect").selectByText("BUG");
        $(SelectElement.class).id("statusSelect").selectByText("Fixed");
        $(SelectElement.class).id("assignedToSelect").selectByText("developer");
        $(SelectElement.class).id("reportprojectVersionSelect")
                .selectByText("Version 2");
        Thread.sleep(2000);

        $(ButtonElement.class).id("saveChangesButton").click();
        Thread.sleep(2000);
        
        $(SelectElement.class).id("projectVersionsSelect").selectByText("Version 1");
        
        Thread.sleep(2000);
        
        $(SelectElement.class).id("projectVersionsSelect").selectByText("Version 2");
        
        Thread.sleep(2000);
    }
}
