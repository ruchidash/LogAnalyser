package com.creditsuisse.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.exception.ConnectionException;

public class PropertiesUtil {
	private static final Logger log = LogManager.getLogger(PropertiesUtil.class);

	private static Properties properties = null;

	private PropertiesUtil() {
	}

	private static Properties getProperties() {
		if (properties == null) {
			String propType = getPropertyType();
			String propFile = propType + ".properties";
			try (InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile)) {
				if (inStream == null)
					throw new ConnectionException("No properties file found with name " + propFile);

				properties = new Properties();
				properties.load(inStream);

			} catch (ConnectionException | IOException e) {
				log.error(e.getMessage());
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

	public static int getEventAlertThresold() {
		return Integer.parseInt(getProperties().getProperty("event_alert_thresold"));

	}

	public static String getDriver() {
		return getProperties().getProperty("driver");
	}

	public static String getUrl() {
		return getProperties().getProperty("url");
	}

	public static String getUser() {
		return getProperties().getProperty("user");
	}

	public static String getPassword() {
		return getProperties().getProperty("password");
	}

	public static String getExecutionMode() {
		return getProperties().getProperty("execution_mode");
	}
	
	public static int getThreadPoolSize() {
		return Integer.parseInt(getProperties().getProperty("thread_pool_size"));
	}
	
	public static int getBatchLogSize() {
		return Integer.parseInt(getProperties().getProperty("batch_log_size"));
	}
}
