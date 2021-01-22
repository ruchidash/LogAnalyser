package com.creditsuisse.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.creditsuisse.dao.LogDao;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.factory.LogServiceFactory;
import com.creditsuisse.service.LogService;
import com.creditsuisse.util.ConnectionUtil;

class TestParallelLogService {
	private static final String TABLE_NAME = "EVENT";
	static LogService logService;

	static Connection con;
	static Statement stmt;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		logService = LogServiceFactory.getLogService("parallel");
		con = ConnectionUtil.getConnection();
		stmt = con.createStatement();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		con.close();
	}

	@Test
	void testProcessLogFileWithInvalidPath() throws SQLException, ConnectionException {
		LogDao.dropTable();
		LogDao.initialize();
		logService.processLogFile("invalid/path");

		ResultSet result = stmt.executeQuery("select count(*) rowcount from " + TABLE_NAME + ";");
		assertNotNull(result);

		while (result.next()) {
			assertEquals(0, result.getInt("rowcount"));
		}
	}

	@Test
	void testProcessLogFile() throws SQLException {
		String filePath = new File("src/main/resources/input/testlogfile.txt").getAbsolutePath();
		logService.processLogFile(filePath);

		ResultSet result = stmt.executeQuery("select count(*) rowcount from " + TABLE_NAME + ";");
		assertNotNull(result);

		while (result.next()) {
			assertEquals(6, result.getInt("rowcount"));
		}
	}

}
