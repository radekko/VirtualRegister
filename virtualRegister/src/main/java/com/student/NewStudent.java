package com.student;

import javax.validation.constraints.NotBlank;

public class NewStudent {
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	
	public NewStudent() {}
	
	public NewStudent(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}