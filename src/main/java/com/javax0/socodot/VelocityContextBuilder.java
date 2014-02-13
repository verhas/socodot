package com.javax0.socodot;

import java.util.Map;

import org.apache.velocity.VelocityContext;

public class VelocityContextBuilder {
    final private Map<String, String> pluginParams;
    private String sourceDirectoryName;

    VelocityContextBuilder(Map<String, String> pluginParams) {
        this.pluginParams = pluginParams;
    }

    Map<String, String> getPluginParams() {
        return pluginParams;
    }

    String getSourceDirectoryName() {
        return sourceDirectoryName;
    }

    final private VelocityContext context = new VelocityContext();

    VelocityContext getContext() {
        return context;
    }

    void setSourceDirectoryName(String sourceDirectoryName) {
        this.sourceDirectoryName = sourceDirectoryName;
    }

    public ObjectBuilder singleton(String pluginClassName) {
        return new ObjectBuilder(pluginClassName, this);
    }

    public ScriptBuilder script(String scriptFileName) {
        return new ScriptBuilder(scriptFileName, this);
    }

}
