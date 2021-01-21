package com.creditsuisse.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.ldap.StartTlsRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.dao.util.DBPropertiesUtil;
import com.creditsuisse.entity.Log;
import com.creditsuisse.entity.State;
import com.creditsuisse.exception.ConnectionException;

public class LogDao {
	private static final Logger log = LogManager.getLogger(LogDao.class);

	private static final String TABLE_NAME = "EVENT";
	private static final BigDecimal DURATION_THRESHOLD = new BigDecimal(4); // TODO Move to prop file

	public static void initialize() throws ConnectionException, SQLException {

		Connection con = DBPropertiesUtil.getConnection();
		DatabaseMetaData dbm = con.getMetaData();

		ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
		if (tables.next()) {
			log.info("Table exists");
		} else {
			createTable();
		}
	}

	public static int update(Log log) throws ConnectionException, SQLException {
		Connection con = DBPropertiesUtil.getConnection();
		Statement stmt = con.createStatement();
		int updateCount = 0;
		ResultSet result = stmt
				.executeQuery("select starttime, endtime from " + TABLE_NAME + " where id='" + log.getId() + "';");
		result.next();

		BigDecimal duration = new BigDecimal(0);
		BigDecimal startTime = null;
		BigDecimal endTime = null;

		if (log.getState() == State.FINISHED) {
			startTime = result.getBigDecimal("starttime");
			endTime = log.getTimestamp();
			duration = endTime.subtract(startTime);
		} else if (log.getState() == State.STARTED) {
			startTime = log.getTimestamp();
			endTime = result.getBigDecimal("endtime");
			duration = endTime.subtract(startTime);
		}

		boolean alert = (duration.compareTo(DURATION_THRESHOLD) > 0) ? true : false;
		String query = "UPDATE " + TABLE_NAME + " SET starttime = " + startTime + ", endtime=" + endTime + ", duration="
				+ duration + ", alert=" + alert + " where id='" + log.getId() + "';";
		updateCount = stmt.executeUpdate(query);

		con.commit();
		return updateCount;
	}

	public static void insert(Log log) throws ConnectionException, SQLException {
		Connection con = DBPropertiesUtil.getConnection();
		Statement stmt = con.createStatement();

		BigDecimal startTime = null;
		BigDecimal endTime = null;
		if (log.getState() == State.FINISHED) {
			endTime = log.getTimestamp();
		} else if (log.getState() == State.STARTED) {
			startTime = log.getTimestamp();
		}

		String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + log.getId() + "'," + startTime + "," + endTime
				+ ",'" + log.getHost() + "','" + log.getType() + "', " + null + "," + false + ");";
		stmt.executeUpdate(query);
		con.commit();
	}

	public static void createTable() throws ConnectionException, SQLException {
		Connection con = DBPropertiesUtil.getConnection();
		Statement stmt = con.createStatement();

		String query = "CREATE TABLE " + TABLE_NAME
				+ " (id VARCHAR(50) NOT NULL, starttime bigint,endtime bigint, host VARCHAR(50), type VARCHAR(50), duration bigint, alert boolean, PRIMARY KEY (id));";
		stmt.executeUpdate(query);
		con.commit();
	}

	public static void dumpData() throws ConnectionException, SQLException {
		Connection con = DBPropertiesUtil.getConnection();
		Statement stmt = con.createStatement();

		ResultSet result = stmt.executeQuery("select * from " + TABLE_NAME + ";");
		while (result.next()) {
			System.out.println(
					result.getString("id") + "; " + result.getLong("startTime") + "; " + result.getLong("endTime")
							+ "; " + result.getLong("duration") + "; " + result.getBoolean("alert"));
		}
	}

	public static void dropTable() throws ConnectionException, SQLException {
		Connection con = DBPropertiesUtil.getConnection();
		Statement stmt = con.createStatement();

		String query = "DROP TABLE " + TABLE_NAME + ";";
		stmt.executeUpdate(query);
		con.commit();
	}
}
