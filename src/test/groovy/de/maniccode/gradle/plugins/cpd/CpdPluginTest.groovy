package de.maniccode.gradle.plugins.cpd

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Test

class CpdPluginTest {

	@Test
	public void cpdPluginCanBeApplied() {
		Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'cpd'

        assertTrue(project.plugins.hasPlugin(CpdPlugin))
	}
	
	@Test
	public void cpdPluginCreatesTasks() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'cpd'

		assertTrue(project.tasks.cpdMain instanceof Cpd)
		assertTrue(project.tasks.cpdTest instanceof Cpd)
	}
	
	@Test
	public void cpdTaskHasCorrectInitialValues() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'cpd'
		
		assertEquals("The minimum token count is not set properly.", 50, project.tasks.cpdMain.minimumTokenCount)
		assertEquals("The encoding is not set properly.", "UTF-8", project.tasks.cpdMain.encoding)
		assertEquals("The language is not set properly.", "java", project.tasks.cpdMain.language)
		assertFalse("The ignoreFailures property is not set properly.", project.tasks.cpdMain.ignoreFailures)
		assertFalse("The ignoreLiterals property is not set properly.", project.tasks.cpdMain.ignoreLiterals)
		assertFalse("The ignoreIdentifiers property is not set properly.", project.tasks.cpdMain.ignoreIdentifiers)
		// TODO: check all properties
	}
	
	@Test
	public void xmlReportinIsEnabledByDefault() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'cpd'
		
		assertEquals("The reporting is not set to xml.", "xml", project.tasks.cpdMain.reports.firstEnabled.name)
	}
	
	@Ignore
	public void reportsCanBeSetToCsv() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'cpd'
		
		project.tasks.cpdMain.reports {
			csv.enabled = true
		}
		
		assertEquals("The reporting is not set to csv.", "csv", project.tasks.cpdMain.reports.firstEnabled.name)
	}
	
	// TODO: Fix test
	@Ignore
	public void reportsAreSetToFirstEnabled() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'cpd'
		
		
		project.tasks.cpdMain.reports {
			text.enabled = true
		}
		project.tasks.cpdMain.reports {
			xml.enabled = true
		}
		project.tasks.cpdMain.reports {
			csv.enabled = true
		}
		
		
		assertEquals("The reporting is not set to text.", "xml", project.tasks.cpdMain.reports.firstEnabled.name)
	}

}
