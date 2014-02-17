package com.javax0.socodot.config;

import java.util.Arrays;

public class SetDirectory {
	private final Configuration configuration;

	public SetDirectory(Configuration configuration) {
		this.configuration = configuration;
	}
	public void source(String sourceDirectoryName) {
		configuration.setSourceDirectoryName(sourceDirectoryName);
		
	}

	public void target(String targetDirectoryName) {
		configuration.setTargetDirectoryName(targetDirectoryName);
	}

	public void exclude(String... directoryNamePatterns) {
		configuration.getDirectoryExcludePatterns().addAll(
				Arrays.asList(directoryNamePatterns));
	}
}
