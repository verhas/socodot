package com.javax0.socodot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileWriter {
	public void write(File file, String content, String encoding)
			throws IOException {
		FileUtils.forceMkdir(file.getParentFile());
		FileUtils.write(file, content, encoding);
	}
}
