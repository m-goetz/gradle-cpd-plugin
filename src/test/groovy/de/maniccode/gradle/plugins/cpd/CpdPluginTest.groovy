package de.maniccode.gradle.plugins.cpd

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
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
	}

}
