package de.maniccode.gradle.plugins.cpd;

import org.gradle.api.Project
import org.gradle.api.plugins.quality.CodeQualityExtension

public class CpdExtension extends CodeQualityExtension {
	private final Project project
	
	CpdExtension(Project project) {
		this.project = project
	}
	
	/**
	 * Whether or not this task will ignore failures and continue running the build.
	 */
	boolean ignoreFailures

	/**
	 * Whether or not to write CPD results to {@code System.out}.
	 */
	boolean consoleOutput
	
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
	Integer minimumTokenCount = 50
}
