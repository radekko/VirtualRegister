package com.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.core.resources.WrapperCollectionResources;
import com.exceptions.EntityNotExistException;
import com.person.subject.SubjectResource;
import com.person.subject.SubjectResourceAssembler;

@Component 
public class PersonResourceAssembler implements WrapperCollectionResources<PersonResource, Person>{
	
	private final SubjectResourceAssembler subjectResourceAssemb;
	
	public PersonResourceAssembler(SubjectResourceAssembler subjectResourceAssemb) {
		this.subjectResourceAssemb = subjectResourceAssemb;
	}
	
	@Override
	public PersonResource entitytoResource(Person person) {
		List<SubjectResource> subjectsResources = 
				person.getSubjects().stream().map(subjectResourceAssemb::entitytoResource).collect(Collectors.toList());
		
		PersonResource personResource = new PersonResource(person,subjectsResources);
		addLinks(person, personResource);
		return personResource;
	}
	
	@Override
	public Link linkToGlobalCollection() {
		return linkTo(methodOn(PersonController.class).getAllPersons()).withRel("persons");
	}
	
	private void addLinks(Person person, PersonResource personResource) {
		try {personResource.add(linkToSelf(person));}
		catch (EntityNotExistException e) {throw new RuntimeException("Person not exist");}
		personResource.add(linkToGlobalCollection());
	}
	
	private Link linkToSelf(Person person) throws EntityNotExistException {
		return linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel();
	}
	
}
