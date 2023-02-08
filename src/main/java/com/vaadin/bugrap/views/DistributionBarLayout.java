package com.vaadin.bugrap.views;

import javax.annotation.security.PermitAll;

import com.vaadin.bugrap.components.DistributionBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@PermitAll
@Route("distributionBar")
public class DistributionBarLayout extends VerticalLayout {
    private TextField closedCount;
    private TextField unAssignedCount;
    private TextField unResolvedCount;
    private DistributionBar distributionBar;

    public DistributionBarLayout() {
        closedCount = new TextField();
        closedCount.setId("closedCount");
        unAssignedCount = new TextField();
        unAssignedCount.setId("unAssignedCount");
        unResolvedCount = new TextField();
        unResolvedCount.setId("unResolvedCount");
        distributionBar = new DistributionBar();

        closedCount.setValue("0");
        closedCount.setLabel("Closed count");
        unAssignedCount.setValue("0");
        unAssignedCount.setLabel("Unassigned Count");
        unResolvedCount.setValue("0");
        unResolvedCount.setLabel("Unresolved Count");

        Button updateCountsButtons = new Button("Update Counts");
        updateCountsButtons.setId("updateCountsButtons");

        updateCountsButtons.addClickListener(event -> {
            distributionBar.setClosed(Integer.parseInt(closedCount.getValue()));
            distributionBar.setUnAssigned(Integer.parseInt(unAssignedCount.getValue()));
            distributionBar.setUnResolved(Integer.parseInt(unResolvedCount.getValue()));
        });
        
        closedCount.addValueChangeListener(event -> {
            distributionBar.setClosed(Integer.parseInt(event.getValue()));
        });

        add(closedCount);
        add(unAssignedCount);
        add(unResolvedCount);
        add(updateCountsButtons);
        add(distributionBar);
    }
}
