package com.person.subject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import com.core.EntityNotExistException;

@Component
public class SubjectsResourceAssembler {

	private final SubjectResourceAssembler sra;
	
	public SubjectsResourceAssembler(SubjectResourceAssembler sra) {
		this.sra = sra;
	}

	public Resources<SubjectResource> toGlobalResource(List<Subject> list) throws EntityNotExistException {
		List<SubjectResource> personResources = list.stream().map(sra::toResource).collect(Collectors.toList());
		
		Resources<SubjectResource> resources = new Resources<>(personResources);
		Link selfLink = linkTo(methodOn(SubjectController.class).getSubjectForPerson(list.get(0).getPerson().getId())).withSelfRel();
		resources.add(selfLink);
		return resources;
	}
	
	public SubjectResource toResource(Subject person) {
		return sra.toResource(person);
	}
}
