package com.person;

import org.springframework.hateoas.core.Relation;

@Relation(value="person", collectionRelation="persons")
public class PersonResource extends EmbeddedResourceSupport {
	private String firstName;
	private String lastName;
	
	public PersonResource(Person person) {
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
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