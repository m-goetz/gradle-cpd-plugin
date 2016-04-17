package de.maniccode.gradle.plugins.cpd;

import org.gradle.api.reporting.ReportContainer;
import org.gradle.api.reporting.SingleFileReport;

public interface CpdReports extends ReportContainer<SingleFileReport> {
	
	/**
     * The cpd XML report
     * <p>
     * This report IS enabled by default.
     *
     * @return The cpd XML report
     */
    SingleFileReport getXml();
}
