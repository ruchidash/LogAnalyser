package com.creditsuisse.entity;

import java.util.LinkedList;
import java.util.List;
import com.creditsuisse.util.LogUtil;

public class LogTask implements Runnable {
	private List<Log> logs = new LinkedList<>();

	public LogTask(List<Log> logs) {
		this.logs = logs;
	}

	@Override
	public void run() {
		for (int i = 0; i < logs.size(); i++) {
			Log log = logs.get(i);
			LogUtil.saveEvent(log);
		}
	}
}
