package com.exceptions;

public class MarkFormatException extends Exception{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "Mark '%f' is not valid";

	    public MarkFormatException(float mark) {
	        super(String.format(MESSAGE_FORMAT, mark));
	    }
}
