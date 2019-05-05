package com.person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import com.person.subject.SubjectsCollectionAssembler;

@Component
public class PersonsAssembler {
	private final SubjectsCollectionAssembler subjectsCollectionAssembler;

	public PersonsAssembler(SubjectsCollectionAssembler subjectsCollectionAssembler) {
		this.subjectsCollectionAssembler = subjectsCollectionAssembler;
	}

	public ResourceSupport toResource(Person person) {
		PersonResource personResource = new PersonResource(person);
		personResource.add(getLinks(person));
		
		Resources<ResourceSupport> subjectResource = subjectsCollectionAssembler.toResource(person.getSubjects());
		personResource.embedResource("personSubjects", subjectResource);

		return personResource;
	}
	
	private List<Link> getLinks(Person person){
		List<Link> links = new ArrayList<>();
		links.add(PersonLinkProvider.linkToPerson(person));
		links.add(PersonLinkProvider.linkToPersonCollection());
		return links;
	}
}