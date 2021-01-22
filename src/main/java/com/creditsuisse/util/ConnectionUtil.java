package com.creditsuisse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.creditsuisse.exception.ConnectionException;

public class ConnectionUtil {

	public static Connection getConnection() throws ConnectionException {
		Connection con = null;

		try {
			Class.forName(PropertiesUtil.getDriver());
			String url = PropertiesUtil.getUrl();
			String user = PropertiesUtil.getUser();
			String password = PropertiesUtil.getPassword();
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException | SQLException e) {
			throw new ConnectionException(e.getMessage());
		}
		return con;
	}
}