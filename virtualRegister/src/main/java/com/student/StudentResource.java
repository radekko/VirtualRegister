package com.student;

import org.springframework.hateoas.core.Relation;

@Relation(value="student", collectionRelation="students")
public class StudentResource extends EmbeddedResourceSupport {
	private String firstName;
	private String lastName;
	
	public StudentResource(Student student) {
		this.firstName = student.getFirstName();
		this.lastName = student.getLastName();
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