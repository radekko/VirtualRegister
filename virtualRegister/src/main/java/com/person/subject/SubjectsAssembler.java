package com.person.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import com.person.PersonLinkProvider;

@Component
public class SubjectsAssembler {
	
	public ResourceSupport toResource(Subject subject) {
		ResourceSupport subjectResource = new SubjectResource(subject);
		if(ifSubjectIsAssignedToPerson(subject)) 
			subjectResource.add(getLinks(subject));
			
		return subjectResource;
	}
	
	private List<Link> getLinks(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(SubjectLinkProvider.linkToSubject(subject));    	                   // persons/1/subjects/English - self
		links.add(SubjectLinkProvider.linkToParentPerson(subject));					   // persons/1 - person
		links.add(PersonLinkProvider.linkToSubjectsCollection(subject.getPerson()));   // persons/1/subjects - subjectsForPerson
		return links;
	}
	
	private boolean ifSubjectIsAssignedToPerson(Subject subject) {
		return subject.getPerson() != null;
	}
	
}