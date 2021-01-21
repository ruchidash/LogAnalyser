package com.creditsuisse.dao.util;

import java.util.Properties;

import com.creditsuisse.exception.ConnectionException;

/**
 * @author ruchismita.dash
 * @Description FilePropertiesUtil is a singleton class which is used to read
 *              properties from Properties file
 */
public class FilePropertiesUtil {

	public static String getInputFilePath() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return properties.getProperty("log_input_file_path");
	}

	public static String getOutputFilePath() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return properties.getProperty("log_output_file_path");
	}

	public static String getUserInputFileName() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return properties.getProperty("log_input_file_name");
	}

	public static String getUserOutputFileName() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return properties.getProperty("log_output_file_name");
	}

	public static String getMaximumFileSizeThreshold() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return properties.getProperty("log_size_threshold");
	}
}
