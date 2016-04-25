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

    /**
     * The cpd text report
     *
     * @return The cpd text report
     */
	SingleFileReport getText();

	/**
     * The cpd CSV report
     *
     * @return The cpd CSV report
     */
	SingleFileReport getCsv();
}
