package com.creditsuisse.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.creditsuisse.dao.LogDao;
import com.creditsuisse.entity.Log;
import com.creditsuisse.entity.State;

class TestLogUtil {
	private static final String TABLE_NAME = "EVENT";
	static Connection con;
	static Statement stmt;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		con = ConnectionUtil.getConnection();
		stmt = con.createStatement();
		LogDao.dropTable();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		con.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		LogDao.createTable();
	}

	@AfterEach
	void tearDown() throws Exception {
		LogDao.dropTable();
	}

	@Test
	void testSaveEventWithOneStartEntry() throws SQLException {
		Log logStartEntry = new Log("startid", State.STARTED, 1491377495213l, null, null);
		LogUtil.saveEvent(logStartEntry);

		ResultSet result = stmt
				.executeQuery("select * from " + TABLE_NAME + " where id='" + logStartEntry.getId() + "';");
		result.next();
		assertNotNull(result);
		assertEquals("startid", result.getString("id"));
		assertEquals(1491377495213l, result.getLong("startTime"));
		assertEquals(0l, result.getLong("endTime"));
		assertEquals(0l, result.getLong("duration"));
		assertFalse(result.getBoolean("alert"));
		assertEquals("", result.getString("host"));
		assertEquals("", result.getString("type"));

	}

	@Test
	void testSaveEventWithOneFinishEntry() throws SQLException {
		Log logStartEntry = new Log("finishid", State.FINISHED, 1491377495240l, null, null);
		LogUtil.saveEvent(logStartEntry);

		ResultSet result = stmt
				.executeQuery("select * from " + TABLE_NAME + " where id='" + logStartEntry.getId() + "';");
		result.next();
		assertNotNull(result);
		assertEquals("finishid", result.getString("id"));
		assertEquals(0l, result.getLong("startTime"));
		assertEquals(1491377495240l, result.getLong("endTime"));
		assertEquals(0l, result.getLong("duration"));
		assertFalse(result.getBoolean("alert"));
		assertEquals("", result.getString("host"));
		assertEquals("", result.getString("type"));

	}

	@Test
	void testSaveEventWithTwoStartEntries() throws SQLException {
		Log firstLogEntry = new Log("id1", State.STARTED, 1491377495213l, null, null);
		LogUtil.saveEvent(firstLogEntry);
		Log secondLogEntry = new Log("id2", State.STARTED, 1491377495213l, null, null);
		LogUtil.saveEvent(secondLogEntry);

		ResultSet result = stmt.executeQuery("select count(*) rowcount from " + TABLE_NAME + ";");
		assertNotNull(result);

		while (result.next()) {
			assertEquals(2, result.getInt("rowcount"));
		}

	}

	@Test
	void testSaveEventWithTwoFinishEntries() throws SQLException {
		Log firstLogEntry = new Log("id1", State.FINISHED, 1491377495213l, null, null);
		LogUtil.saveEvent(firstLogEntry);
		Log secondLogEntry = new Log("id2", State.FINISHED, 1491377495213l, null, null);
		LogUtil.saveEvent(secondLogEntry);

		ResultSet result = stmt.executeQuery("select count(*) rowcount from " + TABLE_NAME + ";");
		assertNotNull(result);

		while (result.next()) {
			assertEquals(2, result.getInt("rowcount"));
		}

	}

	@Test
	void testSaveEventWithOneStartAndOneFinishEntriesWithSameId() throws SQLException {
		Log firstLogEntry = new Log("id1", State.STARTED, 1491377495213l, null, null);
		LogUtil.saveEvent(firstLogEntry);
		Log secondLogEntry = new Log("id1", State.FINISHED, 1491377495240l, null, null);
		LogUtil.saveEvent(secondLogEntry);

		ResultSet result = stmt.executeQuery("select * from " + TABLE_NAME + ";");
		assertNotNull(result);

		result.next();
		assertNotNull(result);
		assertEquals("id1", result.getString("id"));
		assertEquals(1491377495213l, result.getLong("startTime"));
		assertEquals(1491377495240l, result.getLong("endTime"));
		assertEquals(27l, result.getLong("duration"));
		assertTrue(result.getBoolean("alert"));
		assertEquals("", result.getString("host"));
		assertEquals("", result.getString("type"));

	}

	@Test
	void testSaveEventWithOneStartAndOneFinishEntriesWithDifferentIds() throws SQLException {
		Log firstLogEntry = new Log("id1", State.STARTED, 1491377495213l, null, null);
		LogUtil.saveEvent(firstLogEntry);
		Log secondLogEntry = new Log("id2", State.FINISHED, 1491377495240l, null, null);
		LogUtil.saveEvent(secondLogEntry);

		ResultSet result = stmt.executeQuery("select count(*) rowcount from " + TABLE_NAME + ";");
		assertNotNull(result);

		while (result.next()) {
			assertEquals(2, result.getInt("rowcount"));
		}

	}

}
