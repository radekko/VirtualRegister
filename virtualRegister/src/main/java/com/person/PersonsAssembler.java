package com.person;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import com.person.subject.SubjectsAssembler;

@Component
public class PersonsAssembler {
	private final PersonLinkProvider personLinkProvider;
	private final SubjectsAssembler subjectsAssembler;

	public PersonsAssembler(PersonLinkProvider personLinkProvider, SubjectsAssembler subjectsAssembler) {
		this.personLinkProvider = personLinkProvider;
		this.subjectsAssembler = subjectsAssembler;
	}

	public Resources<ResourceSupport> entityListToResource(List<Person> persons) {
		List<ResourceSupport> personResources = persons.stream().map(this::embeddedEntityToResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(personResources, personLinkProvider.getLinksForCollection());
	}
	
	public ResourceSupport singleEntityToResource(Person person) {
		ResourceSupport personResource = personToResource(person);
		personResource.add(personLinkProvider.getLinksForChosenPerson(person));
		return personResource;
	}
	
	private ResourceSupport embeddedEntityToResource(Person person) {
		ResourceSupport personResource = personToResource(person);
		personResource.add(personLinkProvider.getLinksForEmbeddedPerson(person));
		return personResource;
	}
	
	private ResourceSupport personToResource(Person person) {
		Resources<ResourceSupport> subjectResource = embeddedSubjectsToResource(person);
		ResourceSupport personResource = new PersonResource(person,subjectResource);
		return personResource;
	}

	private Resources<ResourceSupport> embeddedSubjectsToResource(Person person) {
		Resources<ResourceSupport> subjectResource = null;
		if(!person.getSubjects().isEmpty())
			subjectResource = subjectsAssembler.entityListToResource(person.getSubjects());
		return subjectResource;
	}
}
