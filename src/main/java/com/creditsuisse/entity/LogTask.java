package com.creditsuisse.entity;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.creditsuisse.dao.LogDao;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.exception.DuplicateInsertException;

public class LogTask implements Runnable {
	private static final Logger log = LogManager.getLogger(LogTask.class);
	private List<Log> logs = new LinkedList<>();

	public LogTask(List<Log> logs) {
		this.logs = logs;
	}

	@Override
	public void run() {
		for (int i = 0; i < logs.size(); i++) {
			Log log = logs.get(i);
			saveEvent(log);
		}
	}

	private static Log saveEvent(Log logEntry) {

		try {
			LogDao.insert(logEntry);
		} catch (ConnectionException e) {
			log.error(e.getMessage());
		} catch (DuplicateInsertException e) {
			try {
				LogDao.update(logEntry);
			} catch (ConnectionException e1) {
				log.error(e.getMessage());
			} catch (SQLException e1) {
				log.error(e.getMessage());
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return logEntry;
	}
}
