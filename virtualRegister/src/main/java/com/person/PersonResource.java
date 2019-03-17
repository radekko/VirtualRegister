package com.person;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.person.subject.SubjectResource;

public class PersonResource extends ResourceSupport{

	private long id;
	private String firstName;
	private String lastName;
	private List<SubjectResource> subjectResource;
	
	public PersonResource() {
	}
	public PersonResource(Person p, List<SubjectResource> subjectResource) {
		this.id = p.getId();
		this.firstName = p.getFirstName();
		this.lastName = p.getLastName();
		this.subjectResource = subjectResource;
	}

	public long getIdent() {
		return id;
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

	public List<SubjectResource> getSubjectResource() {
		return subjectResource;
	}

	public void setSubjectResource(List<SubjectResource> subjectResource) {
		this.subjectResource = subjectResource;
	}
}
