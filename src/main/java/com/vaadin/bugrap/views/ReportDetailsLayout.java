package com.vaadin.bugrap.views;

import java.util.stream.Stream;

import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;

public class ReportDetailsLayout extends VerticalLayout {

	private Grid<Report> grid;
	private GridDataView<Report> gridDataView;
	private Binder<Report> reportBinder;
	private H6 reportSummary;
	private Select<Priority> prioritySelect;
	
	

	public ReportDetailsLayout() {
		reportBinder = new Binder<>(Report.class);
		createReportDetails();
	}

	public void connectGrid() {
		gridDataView = grid.getGenericDataView();
		grid.asSingleSelect().addValueChangeListener(event -> {
			Report report = event.getValue();
			System.out.println(report.getDescription());
			reportSummary.setText(report.getSummary());
			reportBinder.bind(prioritySelect, Report::getPriority, Report::setPriority);
		});
	}
	
	public void createReportDetails() {
		reportSummary= new H6();
		add(reportSummary);
		prioritySelect = new Select<Priority>();
		prioritySelect.setItems(Stream.of(Report.Priority.values()).toList());
		add(prioritySelect);
	}

	public Grid<Report> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Report> grid) {
		this.grid = grid;
	}

}
