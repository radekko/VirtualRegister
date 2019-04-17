package com.exceptions;

public class DegreeFormatException extends Exception{
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE_FORMAT = "Degree '%f' is not valid";

	    public DegreeFormatException(float degree) {
	        super(String.format(MESSAGE_FORMAT, degree));
	    }
}
