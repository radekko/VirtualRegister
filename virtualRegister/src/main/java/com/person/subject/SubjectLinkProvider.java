package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.person.PersonController;

@Component
public class SubjectLinkProvider {
	
	public List<Link> getLinksForCollection(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(linkToGlobalCollection(subject));
		return links;
	}
	
	public List<Link> getLinksForEmbeddedSubject(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(linkToSelf(subject));
		return links;
	}
	
	public List<Link> getLinksForChosenSubject(Subject subject){
		List<Link> links = new ArrayList<>();
		links.add(linkToSelf(subject));    				// persons/1/subjects/English - self
		links.add(linkToParent(subject));				// persons/1				  - person
		links.add(linkToGlobalCollection(subject));     // persons/1/subjects		  - subjectsForPerson
		return links;
	}
	
	// persons/{personId}/subjects/{subjectName} - self
	private Link linkToSelf(Subject subject) {
		return linkTo(PersonController.class).slash(subject.getPerson().getId()).slash("subjects").slash(subject.getSubjectName()).withSelfRel();
	}
	
	// persons/{personId}/subjects		  - subjectsForPerson
	private Link linkToGlobalCollection(Subject subject) {
		return linkTo(PersonController.class).slash(subject.getPerson().getId()).slash("subjects").withRel("subjectsForPerson");
	}
	
	// persons/{personId} - person
	private Link linkToParent(Subject subject) {
		return linkTo(PersonController.class).slash(subject.getPerson().getId()).withRel("person");
	}
	
}
