package com.creditsuisse.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.dao.LogDao;
import com.creditsuisse.entity.Log;
import com.creditsuisse.entity.LogTask;
import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.service.LogService;
import com.creditsuisse.util.PropertiesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParallelLogService implements LogService {
	private static Logger log = LogManager.getLogger(ParallelLogService.class);

	public void processLogFile(String logFilePath) {

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(PropertiesUtil.getThreadPoolSize());
		final int batchSize = PropertiesUtil.getBatchLogSize();

		try {
			LogDao.dropTable(); // TODO for debugging purpose
			LogDao.initialize();

			try (Stream<String> stream = Files.lines(Paths.get(logFilePath))) {

				List<Log> logs = new LinkedList<>();
				stream.forEach(line -> {
					ObjectMapper mapper = new ObjectMapper();
					try {
						Log logEntry = mapper.readValue(line, Log.class);
						logs.add(logEntry);

						if (logs.size() % batchSize == 0) {
							LogTask task = new LogTask(new LinkedList<Log>(logs));
							executor.execute(task);
							logs.clear();
						}
					} catch (JsonProcessingException e) {
						log.error(e.getMessage());
					}

				});
				if (!logs.isEmpty()) {
					LogTask task = new LogTask(new LinkedList<Log>(logs));
					executor.execute(task);
					logs.clear();
				}
			} catch (IOException e1) {
				log.error(e1.getMessage());
			}

		} catch (ConnectionException | SQLException e) {
			log.error(e.getMessage());
		}

		try {
			executor.shutdown();
			while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
				System.out.println("Not yet. Still waiting for termination");
			}
			LogDao.dumpData(); // TODO for debugging purpose
		} catch (ConnectionException | SQLException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

}
