package com.api.examify.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.examify.payloads.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	//if user does not sends any form data
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){		
		return new ResponseEntity<>(new ExceptionResponse("HttpMessageNotReadableException","Form data not found"), HttpStatus.BAD_REQUEST);
	}
	
	
	//if any parameter is missing
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){		
		return new ResponseEntity<>(new ExceptionResponse("MissingServletRequestParameterException","Data missing"), HttpStatus.BAD_REQUEST);
	}
	
}
