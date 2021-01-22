package com.creditsuisse.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.entity.Log;
import com.creditsuisse.entity.State;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.exception.DuplicateInsertException;
import com.creditsuisse.util.ConnectionUtil;
import com.creditsuisse.util.PropertiesUtil;

public class LogDao {
	private static final Logger log = LogManager.getLogger(LogDao.class);

	private static final String TABLE_NAME = "EVENT";
	private static final int DURATION_THRESHOLD = PropertiesUtil.getEventAlertThresold(); // TODO Move to prop file

	public static void initialize() throws ConnectionException, SQLException {

		Connection con = ConnectionUtil.getConnection();
		DatabaseMetaData dbm = con.getMetaData();

		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
		if (tables.next()) {
			log.info("Table exists");
		} else {
			createTable();
		}
	}

	public static int update(Log log) throws ConnectionException, SQLException {
		Connection con = ConnectionUtil.getConnection();
		Statement stmt = con.createStatement();
		int updateCount = 0;
		ResultSet result = stmt
				.executeQuery("select starttime, endtime from " + TABLE_NAME + " where id='" + log.getId() + "';");
		result.next();

		long duration = 0;
		long startTime = 0;
		long endTime = 0;

		if (log.getState() == State.FINISHED) {
			startTime = result.getLong("starttime");
			endTime = log.getTimestamp();

		} else if (log.getState() == State.STARTED) {
			startTime = log.getTimestamp();
			endTime = result.getLong("endtime");
		}
		duration = endTime - startTime;
		boolean alert = (duration >= DURATION_THRESHOLD) ? true : false;
		String query = "UPDATE " + TABLE_NAME + " SET starttime = " + startTime + ", endtime=" + endTime + ", duration="
				+ duration + ", alert=" + alert + " where id='" + log.getId() + "';";
		updateCount = stmt.executeUpdate(query);

		con.commit();
		return updateCount;
	}

	public static void insert(Log logEntry) throws ConnectionException, SQLException, DuplicateInsertException {
		Connection con = ConnectionUtil.getConnection();
		Statement stmt = con.createStatement();

		long startTime = 0;
		long endTime = 0;
		if (logEntry.getState() == State.FINISHED) {
			endTime = logEntry.getTimestamp();
		} else if (logEntry.getState() == State.STARTED) {
			startTime = logEntry.getTimestamp();
		}

		String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + logEntry.getId() + "'," + startTime + "," + endTime
				+ ",'" + logEntry.getHost() + "','" + logEntry.getType() + "', " + null + "," + false + ");";
		try {
			stmt.executeUpdate(query);
			con.commit();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new DuplicateInsertException("Record with id " + logEntry.getId() + " already exist.");
		}
	}

	public static void createTable() throws ConnectionException, SQLException {
		Connection con = ConnectionUtil.getConnection();
		Statement stmt = con.createStatement();

		String query = "CREATE TABLE " + TABLE_NAME
				+ " (id VARCHAR(50) NOT NULL, starttime bigint,endtime bigint, host VARCHAR(50), type VARCHAR(50), duration bigint, alert boolean, PRIMARY KEY (id));";
		stmt.executeUpdate(query);
		con.commit();
	}

	public static void dumpData() throws ConnectionException, SQLException {
		Connection con = ConnectionUtil.getConnection();
		Statement stmt = con.createStatement();

		ResultSet result = stmt.executeQuery("select * from " + TABLE_NAME + ";");
		while (result.next()) {
			System.out.println(
					result.getString("id") + "; " + result.getLong("startTime") + "; " + result.getLong("endTime")
							+ "; " + result.getLong("duration") + "; " + result.getBoolean("alert"));
		}
	}

	public static void dropTable() throws ConnectionException, SQLException {
		Connection con = ConnectionUtil.getConnection();
		Statement stmt = con.createStatement();

		String query = "DROP TABLE " + TABLE_NAME + ";";
		stmt.executeUpdate(query);
		con.commit();
	}
}
