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

}
