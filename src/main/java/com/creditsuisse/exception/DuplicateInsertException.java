package com.creditsuisse.exception;

public class DuplicateInsertException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicateInsertException(String message) {
		super(message);
	}

}
