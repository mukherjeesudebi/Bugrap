package com.vaadin.bugrap.components;

import org.junit.Assert;
import org.junit.Test;

public class DistributionBarTest {

    @Test
    public void propertyIsSet() {
        DistributionBar distributionBar = new DistributionBar();
        distributionBar.setClosed(0);
        distributionBar.setUnAssigned(15);
        distributionBar.setUnResolved(7);
        
           
        Assert.assertEquals("Closed count do not match", 0, Integer.parseInt(distributionBar.getElement().getProperty("closed")));
        Assert.assertEquals("unAssigned count do not match",15,Integer.parseInt(distributionBar.getElement().getProperty("unAssigned")));
        Assert.assertEquals("unResolved count do not match", 7,Integer.parseInt(distributionBar.getElement().getProperty("unResolved")));
        
    }
}
