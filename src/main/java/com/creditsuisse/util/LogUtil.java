package com.creditsuisse.util;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.creditsuisse.dao.LogDao;
import com.creditsuisse.entity.Log;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.exception.DuplicateInsertException;

public class LogUtil {
	private static Logger log = LogManager.getLogger(LogUtil.class);
	
	public static Log saveEvent(Log logEntry) {

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
