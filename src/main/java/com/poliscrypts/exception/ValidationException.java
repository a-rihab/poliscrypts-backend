package com.poliscrypts.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException(Map<String, String> errors) {
	}

}
