package com.creditsuisse.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.creditsuisse.exception.ConnectionException;

public class DBPropertiesUtil {
	
	public static Connection getConnection() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		Connection con = null;
		
		try {
			Class.forName(properties.getProperty("driver"));
			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException | SQLException e ) {
			throw new ConnectionException(e.getMessage());
		}
		return con;
	}
	
	public static int getMaximumFileSizeThreshold() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return Integer.parseInt(properties.getProperty("log_size_threshold"));
	}
	
	public static int getBatchInsertCount() throws ConnectionException {
		Properties properties = ConnectionUtil.getProperties();
		return Integer.parseInt(properties.getProperty("log_batch_insert_count"));
	}
}
