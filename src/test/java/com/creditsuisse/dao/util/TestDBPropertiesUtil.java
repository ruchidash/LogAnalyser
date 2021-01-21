package com.creditsuisse.dao.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.creditsuisse.exception.ConnectionException;

class TestDBPropertiesUtil {

	@Test
	void test() throws ConnectionException {
		assertNotNull(DBPropertiesUtil.getConnection());
	}

}
