package com.creditsuisse.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creditsuisse.dao.LogDao;
import com.creditsuisse.entity.Log;
import com.creditsuisse.exception.ConnectionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogHandler {
	public static void main(String[] args) {
		Logger log = LogManager.getLogger(LogHandler.class);

		if (args.length != 1) {
			log.error("No log file path provided");

			throw new IllegalArgumentException("Log file path is needed");
		}

		String filePath = args[0];
		log.info("Raw log file path:- " + filePath);
		
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			LogDao.dropTable();
			LogDao.initialize();
			ObjectMapper mapper = new ObjectMapper();
			
			stream.map(line -> {
				Log logEntry = null;
				try {
					logEntry = mapper.readValue(line, Log.class);
					
					try {
						LogDao.insert(logEntry);
					} catch (ConnectionException e) {
						log.error(e.getMessage());
					} catch (SQLException e) {
						log.error(e.getMessage());
						try {
							LogDao.update(logEntry);
						} catch (ConnectionException e1) {
							log.error(e.getMessage());
						} catch (SQLException e1) {
							log.error(e.getMessage());
						}
					}
					
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

				return logEntry;
			}).collect(Collectors.toList());


			LogDao.dumpData();
		} catch (IOException e1) {
			log.error(e1.getMessage());
		} catch (ConnectionException e1) {
			log.error(e1.getMessage());
		} catch (SQLException e1) {
			log.error(e1.getMessage());
		}
	}
}
