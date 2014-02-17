package com.javax0.socodot;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.output.NullWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javax0.socodot.config.Configuration;

public class Socodot {
	private static final Logger LOG = LoggerFactory.getLogger(Socodot.class);
	private final Configuration configuration = new Configuration();
	private final ContextCalculator contextCalculator = new ContextCalculator(
			configuration);
	private static final String DEFAULT_CONFIG_FILE_NAME = "socodot.config";

	public Socodot(File configurationFile, Map<String, String> pluginParams) {
		configuration.setConfigurationFile(configurationFile);
		configuration.setPluginParams(pluginParams);
	}

	public Socodot(Map<String, String> pluginParams) {
		configuration.setConfigurationFile(new File(DEFAULT_CONFIG_FILE_NAME));
		configuration.setPluginParams(pluginParams);
	}

	public Socodot(File configurationFile) {
		configuration.setConfigurationFile(configurationFile);
	}

	public Socodot() {
		configuration.setConfigurationFile(new File(DEFAULT_CONFIG_FILE_NAME));
	}

	public void execute() {
		VelocityContext context = contextCalculator.calculateContext();
		processSources(context);
	}

	private void processSources(VelocityContext pluginContext) {
		Properties configuration = new Properties();
		configuration.put("file.resource.loader.path", new File(
				this.configuration.getSourceDirectoryName()).getAbsolutePath());
		VelocityEngine engine = new VelocityEngine(configuration);
		VelocityContext context = new VelocityContext(pluginContext);
		processSourceFiles(engine, context);
	}

	private SourceFileLister sourceFileLister = new SourceFileLister(
			configuration);

	private void processSourceFiles(VelocityEngine engine,
			VelocityContext context) {

		for (File sourceFile : sourceFileLister.sourceFiles()) {
			processSourceFile(engine, context, sourceFile, true);
		}
	}

	private FileWriter writer = new FileWriter();

	public void setWriter(FileWriter writer) {
		this.writer = writer;
	}

	private void processSourceFile(VelocityEngine engine,
			VelocityContext context, File sourceFile, boolean shouldWriteOutput) {
		final Template sourceTemplate;
		try {
			final String relativeFileName = configuration
					.calculateRelativeSourceFileName(sourceFile);
			sourceTemplate = engine.getTemplate(relativeFileName, "UTF-8");
			final StringWriter sw = shouldWriteOutput ? new StringWriter()
					: null;
			final NullWriter nw = shouldWriteOutput ? null : new NullWriter();
			final Writer w = shouldWriteOutput ? sw : nw;
			sourceTemplate.merge(context, w);
			if (shouldWriteOutput) {
				String content = sw.toString();
				sw.close();
				File file = new File(configuration.getTargetDirectoryName()
						+ "/" + relativeFileName);
				writer.write(file, content, "UTF-8");
			} else {
				nw.close();
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
	}
}
