package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.exceptions.EntityNotExistException;
import com.person.PersonController;

@Component
public class SubjectLinkProvider {
	
	// persons/{personId}/subjects/{subjectName} - self
	public static Link linkToSubject(Subject subject) {
		long personId = subject.getPerson().getId();
		String subjectName = subject.getSubjectName();

		Link link;
		try {
			link = linkTo(methodOn(SubjectController.class).getSubjectForPersonBySubjectName(personId,subjectName)).withSelfRel();
		} 
		catch (EntityNotExistException e) {throw new RuntimeException("Entity not exist");}
		
		return link;
	}
	
	// persons/{personId} - person
	public static Link linkToParentPerson(Subject subject) {
		long personId = subject.getPerson().getId();

		Link link;
		try {link = linkTo(methodOn(PersonController.class).getPerson(personId)).withRel("person");}
		catch (EntityNotExistException e) {throw new RuntimeException("Person not exist");}
		
		return link;
	}
	
}
