package com.creditsuisse.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TestPropertiesUtil {

	@Test
	void testGetUrl() {
		assertNotNull(PropertiesUtil.getUrl());
	}

	@Test
	void testGetPassword() {
		assertNotNull(PropertiesUtil.getPassword());
	}

	@Test
	void testGetBatchLogSize() {
		assertNotNull(PropertiesUtil.getBatchLogSize());
	}

	@Test
	void testGetEventAlertThresold() {
		assertNotNull(PropertiesUtil.getEventAlertThresold());
	}

	@Test
	void testGetExecutionMode() {
		assertNotNull(PropertiesUtil.getExecutionMode());
	}

	@Test
	void testGetThreadPoolSize() {
		assertNotNull(PropertiesUtil.getThreadPoolSize());
	}

}
