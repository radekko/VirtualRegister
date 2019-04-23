package com.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.exceptions.EntityNotExistException;
import com.person.subject.SubjectController;

@Component
public class PersonLinkProvider {
	
	// /persons
	public static Link linkToPersonCollection() {
		return linkTo(methodOn(PersonController.class).getAllPersons()).withRel("persons");
	}
	
	// /persons/{personId}
	public static Link linkToPerson(Person person){
		Link link;
		try {link = linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel();}
		catch (EntityNotExistException e) {throw new RuntimeException("Person not exist");}
		return link;
	}
	
	
	// persons/{personId}/subjects - subjectsForPerson
	public static Link linkToSubjectsCollection(Person person) {
		long personId = person.getId();
		
		Link link;
		try {
			link = linkTo(methodOn(SubjectController.class).getSubjectsForPerson(personId)).withRel("subjectsForPerson");
		} 
		catch (EntityNotExistException e) {throw new RuntimeException("Entity not exist");}
		
		return link;
	}
}
