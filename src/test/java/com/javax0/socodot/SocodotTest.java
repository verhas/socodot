package com.javax0.socodot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@Test
	public void itConfiguresAndHandlesParameterlessObjects() throws IOException {
		Socodot engine = new Socodot();
		FileWriter fw = Mockito.mock(FileWriter.class);
		engine.setWriter(fw);
		engine.execute();
		Mockito.verify(fw).write(Mockito.any(File.class),
				Mockito.eq("\r\n\r\n\r\n"), Mockito.eq("UTF-8"));
	}

	@Test
	public void itConfiguresAndHandlesParameteredObjects() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("variable", "value");
		Socodot engine = new Socodot(new File("socodot.config"), params);
		engine.execute();
	}
}
