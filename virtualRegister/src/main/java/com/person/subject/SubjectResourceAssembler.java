package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.stereotype.Component;

import com.core.CollectionResources;
import com.person.PersonController;

@Component
public class SubjectResourceAssembler implements CollectionResources<SubjectResource,Subject>{

	public SubjectResourceAssembler() {}
	
	public SubjectResource toResource(Subject subject) {
		SubjectResource sr = new SubjectResource(subject);
		if(ifPersonsAreAssignedToSubject(subject)) 
			addPersonLinks(subject, sr);
		
		return sr;
	}

	private boolean ifPersonsAreAssignedToSubject(Subject subject) {
		return subject.getPerson() != null;
	}

	private void addPersonLinks(Subject subject, SubjectResource sr) {
		sr.add(linkTo(PersonController.class).slash(subject.getPerson().getId()).slash("subjects").slash(subject.getSubjectName()).withSelfRel());
		sr.add(linkTo(PersonController.class).slash(subject.getPerson().getId()).withRel("person"));
	}
}
