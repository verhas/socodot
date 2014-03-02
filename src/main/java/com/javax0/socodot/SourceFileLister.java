package com.javax0.socodot;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.javax0.socodot.config.Configuration;

class SourceFileLister {
	private final Configuration configuration;
	
	public SourceFileLister(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	public Collection<File> sourceFiles() {
		File sourceDirectory = new File(configuration.getSourceDirectoryName());
		return FileUtils.listFiles(sourceDirectory, FileFilterUtils
				.notFileFilter(new WildcardFileFilter(configuration
						.getFileExcludePatterns())), FileFilterUtils
				.notFileFilter(new WildcardFileFilter(configuration
						.getDirectoryExcludePatterns())));
	}
}
