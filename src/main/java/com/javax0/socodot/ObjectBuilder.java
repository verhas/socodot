package com.javax0.socodot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.velocity.VelocityContext;

public class ObjectBuilder {
    private final String pluginClassName;
    private final VelocityContext context;
    final private Map<String, String> pluginParams;

    ObjectBuilder(String pluginClassName, VelocityContextBuilder contextBuilder) {
        this.pluginClassName = pluginClassName;
        this.context = contextBuilder.getContext();
        this.pluginParams = contextBuilder.getPluginParams();
    }

    public void as(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException {
        Object pluginObject;
        try {
            final Constructor<?> constructor = Class.forName(pluginClassName).getConstructor(Map.class);
            pluginObject = constructor.newInstance(pluginParams);

        } catch (NoSuchMethodException e) {
            pluginObject = Class.forName(pluginClassName).newInstance();
        }
        context.put(name, pluginObject);
    }
}
