package com.javax0.socodot.config;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Configuration {
	private final List<String> fileExcludePatterns = new LinkedList<String>();
	private static final String DEFAULT_SOURCE_DIRECTORY_NAME = "src/wiki";
	private static final String DEFAULT_TARGET_DIRECTORY_NAME = "target";
	private String sourceDirectoryName = DEFAULT_SOURCE_DIRECTORY_NAME;
	private String targetDirectoryName = DEFAULT_TARGET_DIRECTORY_NAME;
	private int sourceDirectoryNamePrefixLength;
	private final List<String> directoryExcludePatterns = new LinkedList<String>();
	private File configurationFile;
	private Map<String, String> pluginParams = null;
	
	public List<String> getFileExcludePatterns() {
		return fileExcludePatterns;
	}

	public String getSourceDirectoryName() {
		return sourceDirectoryName;
	}

	public String getTargetDirectoryName() {
		return targetDirectoryName;
	}

	public int getSourceDirectoryNamePrefixLength() {
		return sourceDirectoryNamePrefixLength;
	}

	public List<String> getDirectoryExcludePatterns() {
		return directoryExcludePatterns;
	}

	public void setSourceDirectoryName(String sourceDirectoryName) {
		this.sourceDirectoryName = sourceDirectoryName;
		calculateSourceDirectoryNamePrefixLength();
	}

	public void setTargetDirectoryName(String targetDirectoryName) {
		this.targetDirectoryName = targetDirectoryName;
	}

	public void setSourceDirectoryNamePrefixLength(
			int sourceDirectoryNamePrefixLength) {
		this.sourceDirectoryNamePrefixLength = sourceDirectoryNamePrefixLength;
	}

	private void calculateSourceDirectoryNamePrefixLength() {
		setSourceDirectoryNamePrefixLength(new File(getSourceDirectoryName())
				.getAbsolutePath().length() + 1);
	}

	public String calculateRelativeSourceFileName(File sourceFile) {
		return sourceFile.getAbsolutePath().substring(
				getSourceDirectoryNamePrefixLength());
	}

	public File getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public Map<String, String> getPluginParams() {
		return pluginParams;
	}

	public void setPluginParams(Map<String, String> pluginParams) {
		this.pluginParams = pluginParams;
	}
	
}
