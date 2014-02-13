package com.javax0.socodot;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.output.NullWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socodot {
    private static final Logger LOG = LoggerFactory.getLogger(Socodot.class);
    private static final String DEFAULT_CONFIG_FILE_NAME = "socodot.config";
    private static final String DEFAULT_SOURCE_DIRECTORY_NAME = "src/wiki";
    private static final String DEFAULT_TARGET_DIRECTORY_NAME = "target";
    private final File configurationFile;
    private String sourceDirectoryName;
    private String targetDirectoryName;
    private Map<String, String> pluginParams = null;
    private int sourceDirectoryNamePrefixLength;
    private List<String> directoryExcludePatterns = new LinkedList<String>();
    private List<String> fileExcludePatterns = new LinkedList<String>();

    public Socodot(File configurationFile, Map<String, String> pluginParams) {
        setDefaultDirectoryNames();
        this.pluginParams = pluginParams;
        this.configurationFile = configurationFile;
    }

    public Socodot(Map<String, String> pluginParams) {
        setDefaultDirectoryNames();
        this.pluginParams = pluginParams;
        this.configurationFile = getDefaultConfigurationFile();
    }

    public Socodot(File configurationFile) {
        setDefaultDirectoryNames();
        this.configurationFile = configurationFile;
    }

    public Socodot() {
        setDefaultDirectoryNames();
        this.configurationFile = getDefaultConfigurationFile();
    }

    private File getDefaultConfigurationFile() {
        return new File(DEFAULT_CONFIG_FILE_NAME);
    }

    private void setDefaultDirectoryNames() {
        this.sourceDirectoryName = DEFAULT_SOURCE_DIRECTORY_NAME;
        calculateSourceDirectoryNamePrefixLength();
        this.targetDirectoryName = DEFAULT_TARGET_DIRECTORY_NAME;
    }

    public class SetDirectory {
        public void source(String sourceDirectoryName) {
            Socodot.this.sourceDirectoryName = sourceDirectoryName;
            calculateSourceDirectoryNamePrefixLength();
        }

        public void target(String targetDirectoryName) {
            Socodot.this.targetDirectoryName = targetDirectoryName;
        }

        public void exclude(String... directoryNamePatterns) {
            directoryExcludePatterns.addAll(Arrays.asList(directoryNamePatterns));
        }
    }

    public class SetFile {
        public void exclude(String... fileNamePatterns) {
            fileExcludePatterns.addAll(Arrays.asList(fileNamePatterns));
        }
    }

    private void calculateSourceDirectoryNamePrefixLength() {
        this.sourceDirectoryNamePrefixLength = new File(sourceDirectoryName).getAbsolutePath().length() + 1;
    }

    public void execute() {
        VelocityContext context = calculateContext();
        processSources(context);
    }

    private VelocityContext calculateContext() {
        VelocityEngine engine = createConfiguredVelocityEngine();

        final VelocityContextBuilder contextBuilder = new VelocityContextBuilder(pluginParams);
        contextBuilder.setSourceDirectoryName(sourceDirectoryName);

        VelocityContext context = new VelocityContext();
        context.put("register", contextBuilder);

        context.put("directory", new SetDirectory());

        context.put("file", new SetFile());

        processConfigurationFile(engine, context);
        return contextBuilder.getContext();
    }

    private VelocityEngine createConfiguredVelocityEngine() {
        Properties configuration = new Properties();
        configuration.put("file.resource.loader.path", configurationFile.getAbsoluteFile().getParent());
        return new VelocityEngine(configuration);
    }

    private void processConfigurationFile(VelocityEngine engine, VelocityContext context) {
        final Template macroTemplate;
        macroTemplate = engine.getTemplate(configurationFile.getName(), "UTF-8");
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

    private void processSources(VelocityContext pluginContext) {
        Properties configuration = new Properties();
        configuration.put("file.resource.loader.path", new File(sourceDirectoryName).getAbsolutePath());
        VelocityEngine engine = new VelocityEngine(configuration);
        VelocityContext context = new VelocityContext(pluginContext);
        processSourceFiles(engine, context);
    }

    private void processSourceFiles(VelocityEngine engine, VelocityContext context) {

        for (File sourceFile : sourceFiles()) {
            processSourceFile(engine, context, sourceFile, true);
        }
    }

    private Collection<File> sourceFiles() {
        File sourceDirectory = new File(sourceDirectoryName);
        return FileUtils.listFiles(sourceDirectory, FileFilterUtils.notFileFilter(new WildcardFileFilter(fileExcludePatterns)),
                        FileFilterUtils.notFileFilter(new WildcardFileFilter(directoryExcludePatterns)));
    }

    private void processSourceFile(VelocityEngine engine, VelocityContext context, File sourceFile, boolean shouldWriteOutput) {
        final Template sourceTemplate;
        try {
            final String relativeFileName = relativeSourceFileName(sourceFile);
            sourceTemplate = engine.getTemplate(relativeFileName, "UTF-8");
            final StringWriter sw = shouldWriteOutput ? new StringWriter() : null;
            final NullWriter nw = shouldWriteOutput ? null : new NullWriter();
            final Writer w = shouldWriteOutput ? sw : nw;
            sourceTemplate.merge(context, w);
            if (shouldWriteOutput) {
                writeStringToFile(sw.toString(), relativeFileName);
                sw.close();
            }else{
                nw.close();
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    private void writeStringToFile(String content, String relativeFileName) throws IOException {
        File output = new File(targetDirectoryName + "/" + relativeFileName);
        FileUtils.forceMkdir(output.getParentFile());
        FileUtils.write(output, content, "UTF-8");
    }

    private String relativeSourceFileName(File sourceFile) {
        return sourceFile.getAbsolutePath().substring(sourceDirectoryNamePrefixLength);
    }
}
