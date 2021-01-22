package com.creditsuisse.dao.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.creditsuisse.exception.ConnectionException;
import com.creditsuisse.util.ConnectionUtil;

class TestDBPropertiesUtil {

	@Test
	void test() throws ConnectionException {
		assertNotNull(ConnectionUtil.getConnection());
	}

}
