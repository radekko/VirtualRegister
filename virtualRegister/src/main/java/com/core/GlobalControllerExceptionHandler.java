package com.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler({ EntityNotExistException.class})
	public ResponseEntity<ExceptionObject> handleNotFounds(Exception e) {
        return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
