package com.javax0.socodot;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.VelocityContext;

public class ScriptBuilder {
    private final String scriptFileName;
    private final VelocityContext context;
    private final Map<String, String> pluginParams;
    private final String sourceDirectoryName;

    ScriptBuilder(String scriptName, VelocityContextBuilder contextBuilder) {
        this.scriptFileName = scriptName;
        this.context = contextBuilder.getContext();
        this.pluginParams = contextBuilder.getPluginParams();
        this.sourceDirectoryName = contextBuilder.getSourceDirectoryName();
    }

    public void as(String name) throws IOException, ScriptException, URISyntaxException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByExtension(FilenameUtils.getExtension(scriptFileName));
        if (engine == null) {
            throw new RuntimeException("No script engine was found for script '" + scriptFileName + "'");
        }
        final File scriptFile = new File(sourceDirectoryName + "/" + scriptFileName);
        final URL resourceUrl = this.getClass().getClassLoader().getResource(scriptFileName);
        final File scriptResorce = resourceUrl == null ? null : new File(resourceUrl.toURI());
        final String script;
        if (scriptFile.exists()) {
            script = FileUtils.readFileToString(scriptFile);
        } else if (scriptResorce != null && scriptResorce.exists()) {
            script = FileUtils.readFileToString(scriptResorce);
        } else {
            throw new RuntimeException("'" + scriptFileName + "' can not be found in source directory or as resource");
        }
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        if (pluginParams != null) {
            bindings.putAll(pluginParams);
        }
        engine.eval(script);
        context.put(name, engine);
    }
}
