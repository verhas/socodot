package com.javax0.socodot.config;

import java.util.Arrays;

public class SetFile {
	private final Configuration configuration;

	public SetFile(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public void exclude(String... fileNamePatterns) {
		configuration.getFileExcludePatterns().addAll(Arrays.asList(fileNamePatterns));
	}
}
