package com.vaadin.bugrap.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Tag("distribution-bar")
@JsModule("./distribution-bar.ts")
public class DistributionBar extends Component {
    private Integer closed;
    private Integer unAssigned;
    private Integer unResolved;
    private String closedWidth;
    private String unResolvedWidth;
    private String unAssignedWidth;

    public DistributionBar() {
        closed = 0;
        unAssigned = 0;
        unResolved = 0;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
        getElement().setProperty("closed", this.closed);
        setWidthLayout();
    }

    public Integer getUnAssigned() {
        return unAssigned;
    }

    public void setUnAssigned(Integer unAssigned) {
        this.unAssigned = unAssigned;
        getElement().setProperty("unAssigned", this.unAssigned);
        setWidthLayout();
    }

    public Integer getUnResolved() {
        return unResolved;
    }

    public void setUnResolved(Integer unResolved) {
        this.unResolved = unResolved;
        getElement().setProperty("unResolved", this.unResolved);
        setWidthLayout();
    }

    public String getClosedWidth() {
        return closedWidth;
    }

    public void setClosedWidth(String closedWidth) {
        this.closedWidth = closedWidth;
        getElement().setProperty("closedWidth", this.closedWidth);
    }

    public String getUnResolvedWidth() {
        return unResolvedWidth;
    }

    public void setUnResolvedWidth(String unResolvedWidth) {
        this.unResolvedWidth = unResolvedWidth;
        getElement().setProperty("unResolvedWidth", this.unResolvedWidth);
    }

    public String getUnAssignedWidth() {
        return unAssignedWidth;
    }

    public void setUnAssignedWidth(String unAssignedWidth) {
        this.unAssignedWidth = unAssignedWidth;
        getElement().setProperty("unAssignedWidth", this.unAssignedWidth);
    }

    public void setWidthLayout() {
        int totalCount = this.closed + this.unAssigned + this.unResolved;
        if (totalCount >= 13) {
            this.setClosedWidth(
                    Math.round(((float) this.closed / totalCount) * 100) + "%");
            this.setUnResolvedWidth(
                    Math.round(((float) this.unResolved / totalCount) * 100)
                            + "%");
            this.setUnAssignedWidth(
                    Math.round(((float) this.unAssigned / totalCount) * 100)
                            + "%");
        } else {
            this.setClosedWidth(this.closed * 30 + "px");
            this.setUnResolvedWidth(this.unResolved * 30 + "px");
            this.setUnAssignedWidth(this.unAssigned * 30 + "px");
        }

    }

}
