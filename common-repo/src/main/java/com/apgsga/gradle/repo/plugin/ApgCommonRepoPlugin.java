/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.apgsga.gradle.repo.plugin;

import com.apgsga.gradle.repo.extensions.ReposImpl;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;

@NonNullApi
public class ApgCommonRepoPlugin implements Plugin<Project> {

	public static final String COMMMON_REPO_PLUGIN_NAME = "apgRepos";

	@Override
	public void apply(final Project project) {
		final ExtensionContainer ext = project.getExtensions();
		ext.create(COMMMON_REPO_PLUGIN_NAME, ReposImpl.class, project);
	}
}