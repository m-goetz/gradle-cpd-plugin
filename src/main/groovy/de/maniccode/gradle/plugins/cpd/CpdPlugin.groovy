package de.maniccode.gradle.plugins.cpd;

import org.gradle.api.plugins.quality.CodeQualityExtension
import org.gradle.api.plugins.quality.internal.AbstractCodeQualityPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.util.VersionNumber

public class CpdPlugin extends AbstractCodeQualityPlugin<Cpd> {
	public static final String DEFAULT_PMD_VERSION = "5.2.3"
	private CpdExtension extension
	
	
	@Override
	protected Class<Cpd> getTaskType() {
		return Cpd
	}

	@Override
	protected String getToolName() {
		return "Cpd"
	}

	@Override
	protected CodeQualityExtension createExtension() {
		extension = project.extensions.create('cpd', CpdExtension, project)

        extension.with {
			ignoreFailures = false
			consoleOutput = false
			encoding = 'UTF-8'
			ignoreLiterals = false
			ignoreIdentifiers = false
			language = 'java'
			minimumTokenCount = 50
			toolVersion = DEFAULT_PMD_VERSION
        }

        return extension
	}
	
	@Override
	protected void configureTaskDefaults(Cpd task, String baseName) {
		def config = project.configurations['cpd']
		config.defaultDependencies { dependencies ->
			VersionNumber version = VersionNumber.parse(this.extension.toolVersion)
			String dependency = calculateDefaultDependencyNotation(version)
			dependencies.add(this.project.dependencies.create(dependency))
		}
		task.conventionMapping.with {
			pmdClasspath = { config }
			ignoreFailures = { extension.ignoreFailures }
			consoleOutput = { extension.consoleOutput }
			encoding = { extension.encoding }
			ignoreLiterals = { extension.ignoreLiterals }
			ignoreIdentifiers = { extension.ignoreIdentifiers }
			language = { extension.language }
			minimumTokenCount = { extension.minimumTokenCount }
			
			task.reports.xml.conventionMapping.with {
				enabled = { true }
				destination = { new File(extension.reportsDir, "${baseName}.xml") }
			}
			task.reports.text.conventionMapping.with {
				destination = { new File(extension.reportsDir, "${baseName}.txt") }
			}
			task.reports.csv.conventionMapping.with {
				destination = { new File(extension.reportsDir, "${baseName}.cvs") }
			}
		}
	}
	
	
	private String calculateDefaultDependencyNotation(VersionNumber toolVersion) {
		if (toolVersion < VersionNumber.version(5)) {
			return "pmd:pmd:$extension.toolVersion"
		} else if (toolVersion < VersionNumber.parse("5.2.0")) {
			return "net.sourceforge.pmd:pmd:$extension.toolVersion"
		}
		return "net.sourceforge.pmd:pmd-java:$extension.toolVersion"
	}

	@Override
	protected void configureForSourceSet(SourceSet sourceSet, Cpd task) {
		task.with {
			description = "Run CPD analysis for ${sourceSet.name} classes"
		}
		task.setSource(sourceSet.allJava)
	}
}
