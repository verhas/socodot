package com.javax0.socodot;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SocodotTest {
    private static final Logger LOG = LoggerFactory
            .getLogger(SocodotTest.class);

    public static class LoggerPlugin {
        public LoggerPlugin(Map<String, String> params) {
            if (params != null) {
                LOG.debug("Was initialized for '" + params.get("variable")
                        + "'");
            }
        }

        public void debug(String message) {
            LOG.debug(message);
        }
    }

    private final File TEST_CONFIG;

    public SocodotTest() throws URISyntaxException {
        TEST_CONFIG = new File(this.getClass().getClassLoader().getResource("socodot.test.config").toURI());
    }

    @Test
    public void itConfiguresAndHandlesParameterlessObjects() throws IOException {
        final Socodot engine = new Socodot(TEST_CONFIG);
        FileWriter fw = Mockito.mock(FileWriter.class);
        engine.setWriter(fw);
        engine.execute();
        Mockito.verify(fw).write(Mockito.any(File.class),
                Mockito.eq("\r\n\r\n\r\n"), Mockito.eq("UTF-8"));
    }

    @Test
    public void itConfiguresAndHandlesParameteredObjects() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("variable", "value");
        final Socodot engine = new Socodot(TEST_CONFIG, params);
        FileWriter fw = Mockito.mock(FileWriter.class);
        engine.setWriter(fw);
        engine.execute();
        Mockito.verify(fw).write(Mockito.any(File.class),
                Mockito.eq("\r\n\r\n\r\n"), Mockito.eq("UTF-8"));
    }
}
