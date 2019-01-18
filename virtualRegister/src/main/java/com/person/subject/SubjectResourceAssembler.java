package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.stereotype.Component;

import com.person.PersonController;

@Component
public class SubjectResourceAssembler {

	public SubjectResourceAssembler() {}

	public SubjectResource toResource(Subject subject) {
		SubjectResource sr = new SubjectResource(subject);
		sr.add(linkTo(PersonController.class).slash(subject.getPerson().getId()).slash("subjects").slash(subject.getSubjectName()).withSelfRel());
		sr.add(linkTo(PersonController.class).slash(subject.getPerson().getId()).withRel("person"));
		return sr;
	}

}
