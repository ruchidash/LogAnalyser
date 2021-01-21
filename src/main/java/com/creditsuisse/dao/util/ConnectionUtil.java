package com.creditsuisse.dao.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.creditsuisse.exception.ConnectionException;

public class ConnectionUtil {
	private static Properties properties = null;

	private ConnectionUtil() {
	}

	public static Properties getProperties() throws ConnectionException {
		if (properties == null) {
			String propType = getPropertyType();
			String propFile = propType + ".properties";
			try (InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile)) {
				if (inStream == null)
					throw new ConnectionException("No properties file found with name " + propFile);

				properties = new Properties();
				properties.load(inStream);

			} catch (ConnectionException | IOException e) {
				throw new ConnectionException("Fail to retrieve property type " + propType);
			}

		}
		return properties;
	}

	private static String getPropertyType() {
		String propType = "";
		propType = System.getenv().get("PROPERTY_TYPE");
		if (propType == null || propType.isEmpty())
			propType = System.getProperty("PROPERTY_TYPE");
		if (propType == null || propType.isEmpty())
			propType = "test";
		return propType;
	}
}