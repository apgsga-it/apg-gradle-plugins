package com.apgsga.gradle.repo.config.plugin;

import com.apgsga.gradle.repo.config.extensions.RepoConfig;
import com.apgsga.gradle.repo.plugin.ApgCommonRepoPlugin;
import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;

@NonNullApi
public class ApgRepoConfigPlugin implements Plugin<Project>{

	@Override
	public void apply(Project project) {
		final ExtensionContainer ext = project.getExtensions();
		final PluginContainer plugins = project.getPlugins();
		plugins.apply(ApgCommonRepoPlugin.class);
		ext.create("apgRepositories", RepoConfig.class, project);
	}
}