package com.exceptions;

public class EntityAlreadyExistException extends Exception{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "Entity '%s' in this student already exist";

	    public EntityAlreadyExistException(String subjectName) {
	        super(String.format(MESSAGE_FORMAT, subjectName));
	    }
}