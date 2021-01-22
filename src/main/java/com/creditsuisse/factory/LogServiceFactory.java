package com.creditsuisse.factory;

import java.util.HashMap;
import java.util.Map;
import com.creditsuisse.service.LogService;
import com.creditsuisse.service.impl.ParallelLogService;
import com.creditsuisse.service.impl.SequentialLogService;

public class LogServiceFactory {
	private static Map<String, LogService> map = new HashMap<>();

	public static LogService getLogService(String executionMode) {
		if (map.size() == 0) {
			map.put("parallel", new ParallelLogService());
			map.put("sequential", new SequentialLogService());
		}

		return map.get(executionMode.toLowerCase());
	}
}
