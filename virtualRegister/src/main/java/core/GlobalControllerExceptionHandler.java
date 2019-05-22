package core;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import exceptions.EntityAlreadyExistException;
import exceptions.EntityNotExistException;
import exceptions.ExceptionObject;
import exceptions.MarkFormatException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler({ EntityNotExistException.class})
	public ResponseEntity<ExceptionObject> handleNotFounds(Exception e) {
        return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ MarkFormatException.class})
	public ResponseEntity<ExceptionObject> handleNotValidMark(Exception e) {
        return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ EntityAlreadyExistException.class})
	public ResponseEntity<ExceptionObject> handleAlreadyExist(Exception e) {
		return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<ExceptionObject> handleConstraintViolation(DataIntegrityViolationException e) {
		return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionObject> handleValidationExceptions(MethodArgumentNotValidException e) {
		return new ResponseEntity<>(new ExceptionObject(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}