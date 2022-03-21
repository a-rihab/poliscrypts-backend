package com.poliscrypts.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.exception.GlobalException;
import com.poliscrypts.util.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandletController {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest req) {
		ExceptionResponse ced = new ExceptionResponse(new Date(), ex.getMessage(), HttpStatus.NOT_FOUND.value(),
				req.getDescription(false));

		return new ResponseEntity<Object>(ced, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<Object> handleCustomException(GlobalException ex, WebRequest req) {
		ExceptionResponse ced = new ExceptionResponse(new Date(), ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getDescription(false));

		return new ResponseEntity<Object>(ced, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
