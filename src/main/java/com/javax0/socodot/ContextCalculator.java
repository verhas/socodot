package com.javax0.socodot;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javax0.socodot.config.Configuration;
import com.javax0.socodot.config.SetDirectory;
import com.javax0.socodot.config.SetFile;

public class ContextCalculator {
	private static final Logger LOG = LoggerFactory
			.getLogger(ContextCalculator.class);

	final Configuration configuration;

	public ContextCalculator(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	VelocityContext calculateContext() {
		VelocityEngine engine = createConfiguredVelocityEngine();

		final VelocityContextBuilder contextBuilder = new VelocityContextBuilder(
				configuration.getPluginParams());
		contextBuilder.setSourceDirectoryName(configuration
				.getSourceDirectoryName());

		VelocityContext context = new VelocityContext();
		context.put("register", contextBuilder);

		context.put("directory", new SetDirectory(configuration));

		context.put("file", new SetFile(configuration));

		processConfigurationFile(engine, context);
		return contextBuilder.getContext();
	}

	private VelocityEngine createConfiguredVelocityEngine() {
		Properties properties = new Properties();
		properties.put("file.resource.loader.path", configuration
				.getConfigurationFile().getAbsoluteFile().getParent());
		return new VelocityEngine(properties);
	}

	private void processConfigurationFile(VelocityEngine engine,
			VelocityContext context) {
		final Template macroTemplate;
		macroTemplate = engine.getTemplate(configuration.getConfigurationFile()
				.getName(), "UTF-8");
		final StringWriter sw = new StringWriter();
		macroTemplate.merge(context, sw);
		logOutputIfThereIsAny(sw);
	}

	private void logOutputIfThereIsAny(StringWriter sw) {
		String configurationOutput = sw.toString().trim();
		if (configurationOutput.length() > 0) {
			LOG.debug(configurationOutput);
		}
	}
}
