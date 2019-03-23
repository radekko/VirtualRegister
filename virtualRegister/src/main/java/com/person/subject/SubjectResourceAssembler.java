package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.core.EmbeddedCollectionResources;
import com.person.PersonController;

@Component
public class SubjectResourceAssembler implements EmbeddedCollectionResources<SubjectResource, Subject>{

	private Long personId;
	
	public SubjectResourceAssembler() {}

	@Override
	public SubjectResource entitytoResource(Subject subject) {
		this.personId = subject.getPerson().getId();
		SubjectResource sr = new SubjectResource(subject);
		if(ifSubjectIsAssignedToPerson(subject)) 
			addLinksToSingleResource(subject, sr);
		
		return sr;
	}

	@Override
	public Link linkToGlobalCollection() {
		return linkTo(PersonController.class).slash(personId).slash("subjects").withRel("subjectsForPerson");
	}
	
	@Override
	public Link linkToParent() {
		return linkTo(PersonController.class).slash(personId).withRel("person");
	}

	private boolean ifSubjectIsAssignedToPerson(Subject subject) {
		return subject.getPerson() != null;
	}

	private void addLinksToSingleResource(Subject subject, SubjectResource sr) {
		sr.add(linkToSelf(subject));    	// persons/1/subjects/English - self
		sr.add(linkToParent());				// persons/1				  - person
		sr.add(linkToGlobalCollection());	// persons/1/subjects		  - subjectsForPerson
	}

	private Link linkToSelf(Subject subject) {
		return linkTo(PersonController.class).slash(personId).slash("subjects").slash(subject.getSubjectName()).withSelfRel();
	}

}
