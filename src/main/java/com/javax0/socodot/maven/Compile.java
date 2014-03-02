package com.javax0.socodot.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.javax0.socodot.Socodot;

@Mojo(name = "compile")
public class Compile extends AbstractMojo {

	@Parameter(alias = "configurationFile", defaultValue = "")
	private String configurationFile;

	private final Log LOG = getLog();

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		LOG.info("Executing socodot compile using configuration '"
				+ configurationFile + "'");
		final Socodot engine;
		if (configurationFile == null || configurationFile.length() == 0) {
			engine = new Socodot();
		} else {
			engine = new Socodot(new File(configurationFile));
		}
		engine.execute();
	}

}
