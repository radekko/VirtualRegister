package com.subjectDetails;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;

import com.exceptions.EntityNotExistException;

public class SubjectDetailsLinkProvider {

	// /subject_details
	public static Link linkToSubjectDetailsCollection() {
		return linkTo(methodOn(SubjectDetailsController.class).getAllSubjectsDetails()).withRel("subjectDetails");
	}

	// /subject_details/{subjectName}
	public static Link linkToSubjectDetails(String subjectName) {
		Link link;
		try {
			link = linkTo(methodOn(SubjectDetailsController.class).getSubjectDetails(subjectName)).withSelfRel();
		} catch (EntityNotExistException e) {
			throw new RuntimeException("Entity not exist");
		}
		return link;
	}
}