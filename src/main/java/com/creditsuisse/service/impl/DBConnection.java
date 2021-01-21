package com.creditsuisse.service.impl;

import java.sql.Connection;

import com.creditsuisse.dao.util.DBPropertiesUtil;
import com.creditsuisse.exception.ConnectionException;

public class DBConnection {
	
	public Connection getConnection() {	
		try {
			return (Connection) DBPropertiesUtil.getConnection();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
