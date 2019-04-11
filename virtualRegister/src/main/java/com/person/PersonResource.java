package com.person;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public class PersonResource extends ResourceSupport{

	private String firstName;
	private String lastName;
	private Resources<ResourceSupport> subjectResource;
	
	public PersonResource() {
	}
	public PersonResource(Person p, Resources<ResourceSupport> subjectResource) {
		this.firstName = p.getFirstName();
		this.lastName = p.getLastName();
		this.subjectResource = subjectResource;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Resources<ResourceSupport> getSubjectResource() {
		return subjectResource;
	}
	public void setSubjectResource(Resources<ResourceSupport> subjectResource) {
		this.subjectResource = subjectResource;
	}

}
