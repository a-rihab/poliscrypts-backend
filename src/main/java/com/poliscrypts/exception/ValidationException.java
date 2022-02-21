package com.poliscrypts.exception;

import java.io.Serializable;
import java.util.Map;

public class ValidationException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	public ValidationException(Map<String, String> errors) {
	}

}
