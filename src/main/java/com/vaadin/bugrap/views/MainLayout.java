package com.vaadin.bugrap.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainLayout extends VerticalLayout {

	private H2 viewTitle;
	
	public MainLayout() {
		viewTitle = new H2("Hello Sudebi");
		add(viewTitle);
	}
}
