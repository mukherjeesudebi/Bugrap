package com.vaadin.bugrap.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Tag("priority-input")
@JsModule("./priority-input.ts")
public class PriorityInput extends Component {
    private Integer priority;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
        getElement().setProperty("priority", this.priority);
    }
}
