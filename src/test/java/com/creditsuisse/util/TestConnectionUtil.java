package com.creditsuisse.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import com.creditsuisse.exception.ConnectionException;

class TestConnectionUtil {
	private static final Logger log = LogManager.getLogger(TestConnectionUtil.class);

	@Test
	void testGetConnection() {
		try {
			assertNotNull(ConnectionUtil.getConnection());
		} catch (ConnectionException e) {
			log.error(e.getMessage());
		}
	}

}
