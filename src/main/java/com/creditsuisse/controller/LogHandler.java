package com.creditsuisse.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.creditsuisse.factory.LogServiceFactory;
import com.creditsuisse.service.LogService;
import com.creditsuisse.util.PropertiesUtil;

public class LogHandler {
	public static void main(String[] args) {
		Logger log = LogManager.getLogger(LogHandler.class);

		if (args.length != 1) {
			log.error("No log file path provided");

			throw new IllegalArgumentException("Log file path is needed");
		}

		String filePath = args[0];
		log.info("Raw log file path:- " + filePath);

		String executionMode = PropertiesUtil.getExecutionMode();
		LogService logService = LogServiceFactory.getLogService(executionMode);

		logService.processLogFile(filePath);
	}
}
