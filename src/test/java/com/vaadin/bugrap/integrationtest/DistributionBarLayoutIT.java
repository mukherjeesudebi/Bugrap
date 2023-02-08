package com.vaadin.bugrap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.openqa.selenium.NotFoundException;

import com.vaadin.bugrap.DistributionBarElement;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class DistributionBarLayoutIT extends DistributionBarLayoutTest {

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
    public void test() throws InterruptedException {
        DistributionBarElement distributionBarElement = $(
                DistributionBarElement.class).first();

        $(TextFieldElement.class).id("closedCount").setValue("10");
        $(TextFieldElement.class).id("unAssignedCount").setValue("50");
        $(TextFieldElement.class).id("unResolvedCount").setValue("5");

        Thread.sleep(50);
        $(ButtonElement.class).id("updateCountsButtons").click();
        Thread.sleep(2000);

        assertEquals(10, ((Long)distributionBarElement.getProperty("closed")).intValue());
        assertEquals(50, ((Long)distributionBarElement.getProperty("unAssigned")).intValue());
        assertEquals(5, ((Long)distributionBarElement.getProperty("unResolved")).intValue());
        
        
        $(TextFieldElement.class).id("closedCount").setValue("3");
        Thread.sleep(1000);
        $(TextFieldElement.class).id("unAssignedCount").setValue("8");
        Thread.sleep(1000);
        $(TextFieldElement.class).id("unResolvedCount").setValue("1");
        Thread.sleep(1000);
        $(ButtonElement.class).id("updateCountsButtons").click();
        Thread.sleep(2000);

        assertEquals(3, ((Long)distributionBarElement.getProperty("closed")).intValue());
        assertEquals(8, ((Long)distributionBarElement.getProperty("unAssigned")).intValue());
        assertEquals(1, ((Long)distributionBarElement.getProperty("unResolved")).intValue());
        
        
    }
}
