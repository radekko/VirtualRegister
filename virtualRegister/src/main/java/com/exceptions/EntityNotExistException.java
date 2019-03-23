package com.exceptions;

public class EntityNotExistException extends Exception{

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "Entity '%d' does not exist";

	    public EntityNotExistException(Long personId) {
	        super(String.format(MESSAGE_FORMAT, personId));
	    }

}
