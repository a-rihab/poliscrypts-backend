package com.poliscrypts.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.poliscrypts.exception.EntityNotFoundException;
import com.poliscrypts.exception.GlobalException;
import com.poliscrypts.util.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandletController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest req) {

		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());

		ExceptionResponse ced = new ExceptionResponse(new Date(), "Record Not Found", details);

		return new ResponseEntity<Object>(ced, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<Object> handleCustomException(GlobalException ex, WebRequest req) {

		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());

		ExceptionResponse ced = new ExceptionResponse(new Date(), "Server Error", details);

		return new ResponseEntity<Object>(ced, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}

		ExceptionResponse ced = new ExceptionResponse(new Date(), "Validation Failed", details);
		return new ResponseEntity<Object>(ced, HttpStatus.BAD_REQUEST);
	}

}
