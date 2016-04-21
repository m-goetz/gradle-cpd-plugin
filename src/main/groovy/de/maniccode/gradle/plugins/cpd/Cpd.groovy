package de.maniccode.gradle.plugins.cpd;

import groovy.util.slurpersupport.GPathResult

import javax.inject.Inject

import org.gradle.api.GradleException
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.reporting.Reporting
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask
import org.gradle.internal.reflect.Instantiator
import org.gradle.logging.ConsoleRenderer

import de.maniccode.gradle.plugins.cpd.internal.CpdReportsImpl

public class Cpd extends SourceTask implements VerificationTask, Reporting<CpdReports> {

	/**
	 * The class path containing the Pmd library to be used.
	 */
	@InputFiles
	FileCollection pmdClasspath;

	@Nested
	private final CpdReportsImpl reports;

	/**
	 * Whether or not this task will ignore failures and continue running the build.
	 */
	boolean ignoreFailures

	/**
	 * Whether or not to write CPD results to {@code System.out}.
	 */
	boolean consoleOutput = true
	
	/**
	 * The character set encoding (e.g., UTF-8) to use when reading the source code files; defaults to locale setting.
	 */
	String encoding
	
	/**
	 * if true, CPD ignores literal value differences when evaluating a duplicate block. 
	 * This means that foo=42; and foo=43; will be seen as equivalent. 
	 * You may want to run PMD with this option off to start with and then switch it on 
	 * to see what it turns up; defaults to false.
	 */
	boolean ignoreLiterals
	
	/**
	 * Similar to ignoreLiterals but for identifiers; i.e., variable names, methods names, 
	 * and so forth; defaults to false.
	 */
	boolean ignoreIdentifiers
	
	/**
	 * Flag to select the appropriate language (e.g. cpp, java, php, ruby); defaults to java.
	 */
	String language
	
	/**
	 * A positive integer indicating the minimum duplicate size.
	 */
	Integer minimumTokenCount
	

	Cpd() {
		reports = instantiator.newInstance(CpdReportsImpl, this)
	}

	@Inject
	Instantiator getInstantiator() {
		throw new UnsupportedOperationException();
	}

	@Inject
	IsolatedAntBuilder getAntBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CpdReports getReports() {
		reports
	}

	@Override
	public CpdReports reports(Closure closure) {
		reports.configure(closure)
	}

	@TaskAction
	public void run() {
		def antCpdArgs = [
			encoding: encoding, 
			format: 'xml', 
			ignoreLiterals: ignoreLiterals, 
			ignoreIdentifiers: ignoreIdentifiers, 
			language: language,
			minimumtokencount: minimumTokenCount,
			outputfile: reports.xml.getDestination()
		]

		antBuilder.withClasspath(getPmdClasspath()).execute { a ->
			ant.taskdef(name: 'cpd', classname: 'net.sourceforge.pmd.cpd.CPDTask')
			ant.cpd(antCpdArgs) {
				getSource().addToAntBuilder(ant, 'fileset', FileCollection.AntType.FileSet)
			}
		}
		
		def errorCount = getCpdErrorCount()
		if (errorCount) {
			def message = "$errorCount CPD rule violations were found."
			def report = reports.firstEnabled
			if (report) {
				def reportUrl = new ConsoleRenderer().asClickableFileUrl(report.destination)
				message += " See the report at: $reportUrl"
			}
			if (getIgnoreFailures()) {
				logger.warn(message)
			} else {
				throw new GradleException(message)
			}
		}
		
	}
	
	private int getCpdErrorCount() {
		File file = reports.xml.getDestination()
		if (file.exists()) {
			GPathResult result = new XmlSlurper().parse(file)
			result.duplication.size()
		}
	}
	
	/**
	 * Validates the value is a valid CPD minimumTokenCount (positive)
	 * @param value rule minimumTokenCount
	 */
	static void validateMinimumTokenCount(int value) {
		if (value < 0) {
			throw new InvalidUserDataException(String.format("Invalid minimumTokenCount '%d'. Must be positive.", value));
		}
	}
}
