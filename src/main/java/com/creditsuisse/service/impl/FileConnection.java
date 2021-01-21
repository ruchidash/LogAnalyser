package com.creditsuisse.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.creditsuisse.dao.util.FilePropertiesUtil;
import com.creditsuisse.exception.ConnectionException;

public class FileConnection {

	public Path getConnection() throws ConnectionException {	
		String filePath = FilePropertiesUtil.getInputFilePath();
		String fileName = FilePropertiesUtil.getUserInputFileName();
		return Paths.get(filePath, fileName);
	}

}
