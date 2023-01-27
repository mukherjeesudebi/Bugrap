package com.vaadin.bugrap.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("reportDetails")
public class ReportDetailsSeparateLayout extends VerticalLayout {
	public ReportDetailsSeparateLayout() {
      H2 h2 = new H2("Testing");
      add(h2);
	}
}
