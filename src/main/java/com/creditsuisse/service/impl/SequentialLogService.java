package com.creditsuisse.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.dao.LogDao;
import com.creditsuisse.entity.Log;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.service.LogService;
import com.creditsuisse.util.LogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SequentialLogService implements LogService {
	private static Logger log = LogManager.getLogger(SequentialLogService.class);

	public void processLogFile(String filePath) {

		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			LogDao.dropTable(); // TODO for debugging purpose
			LogDao.initialize();

			stream.forEach(line -> {
				ObjectMapper mapper = new ObjectMapper();
				Log logEntry;
				try {
					logEntry = mapper.readValue(line, Log.class);
					LogUtil.saveEvent(logEntry);
				} catch (JsonProcessingException e) {
					log.error(e.getMessage());
				}

			});
			LogDao.dumpData(); // TODO for debugging purpose
		} catch (IOException e1) {
			log.error(e1.getMessage());
		} catch (ConnectionException e1) {
			log.error(e1.getMessage());
		} catch (SQLException e1) {
			log.error(e1.getMessage());
		}
	}

}
