package com.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.exceptions.DegreeFormatException;
import com.exceptions.EntityNotExistException;
import com.exceptions.ExceptionObject;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler({ EntityNotExistException.class})
	public ResponseEntity<ExceptionObject> handleNotFounds(Exception e) {
        return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ DegreeFormatException.class})
	public ResponseEntity<ExceptionObject> handleNotValidDegree(Exception e) {
        return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
