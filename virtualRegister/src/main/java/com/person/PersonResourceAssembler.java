package com.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.core.CollectionResources;
import com.person.subject.SubjectResource;
import com.person.subject.SubjectResourceAssembler;

@Component 
public class PersonResourceAssembler implements CollectionResources<PersonResource, Person>{
	
	private final SubjectResourceAssembler subjectResourceAssemb;

	public PersonResourceAssembler(SubjectResourceAssembler subjectResourceAssemb) {
		this.subjectResourceAssemb = subjectResourceAssemb;
	}

	public PersonResource toResource(Person person) {
		List<SubjectResource> subjectsResources = person.getSubjects().stream().map(subjectResourceAssemb::toResource).collect(Collectors.toList());
		
		PersonResource personResource = new PersonResource(person,subjectsResources);
		personResource.add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("persons"));
		personResource.add(linkTo(PersonController.class).slash(person.getId()).withSelfRel());
		return personResource;
	}
	
}
