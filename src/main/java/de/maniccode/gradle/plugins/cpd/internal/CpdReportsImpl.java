package de.maniccode.gradle.plugins.cpd.internal;

import org.gradle.api.Task;
import org.gradle.api.reporting.SingleFileReport;
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport;
import org.gradle.api.reporting.internal.TaskReportContainer;

import de.maniccode.gradle.plugins.cpd.CpdReports;

public class CpdReportsImpl extends TaskReportContainer<SingleFileReport> implements CpdReports {

	public CpdReportsImpl(Task task) {
		super(SingleFileReport.class, task);
		
		add(TaskGeneratedSingleFileReport.class, "xml", task);
		add(TaskGeneratedSingleFileReport.class, "text", task);
		add(TaskGeneratedSingleFileReport.class, "csv", task);
	}

	@Override
	public SingleFileReport getXml() {
		return getByName("xml");
	}
	
	@Override
	public SingleFileReport getText() {
		return getByName("text");
	}
	
	@Override
	public SingleFileReport getCsv() {
		return getByName("csv");
	}

}
