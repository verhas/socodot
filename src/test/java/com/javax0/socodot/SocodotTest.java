package com.javax0.socodot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocodotTest {
    private static final Logger LOG = LoggerFactory.getLogger(SocodotTest.class);

    public static class LoggerPlugin {
        public LoggerPlugin(Map<String, String> params) {
            if (params != null) {
                LOG.debug("Was initialized for '" + params.get("variable") + "'");
            }
        }

        public void debug(String message) {
            LOG.debug(message);
        }
    }

    @Test
    public void itConfiguresAndHandlesParameterlessObjects() {
        Socodot engine = new Socodot();
        engine.execute();
    }

    @Test
    public void itConfiguresAndHandlesParameteredObjects() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("variable", "value");
        Socodot engine = new Socodot(new File("socodot.config"), params);
        engine.execute();
    }
}
