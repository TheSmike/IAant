package it.unipr.scarpenti.ant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

	private static Properties properties = null;

	public static Properties getProperties() throws IOException {
		if (properties == null) {
			properties = new Properties();
			InputStream resourceAsStream = PropertiesFactory.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(resourceAsStream);
		}
		return properties;
	}

}
