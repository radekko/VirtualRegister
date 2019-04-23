package com.person;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public class PersonsCollectionAssembler {

	private final EmbeddedPersonAssambler embeddedPersonAssambler;

	public PersonsCollectionAssembler() {
		this.embeddedPersonAssambler = new EmbeddedPersonAssambler();
	}

	public Resources<ResourceSupport> listToResource(List<Person> persons) {
		List<ResourceSupport> personResources = persons.stream().map(embeddedPersonAssambler::toResource).collect(Collectors.toList());
		return new Resources<ResourceSupport>(personResources, PersonLinkProvider.linkToPersonCollection());
	}

	private class EmbeddedPersonAssambler {

		public ResourceSupport toResource(Person person) {
			ResourceSupport personResource = new PersonResource(person);
			personResource.add(PersonLinkProvider.linkToPerson(person));
			personResource.add(PersonLinkProvider.linkToSubjectsCollection(person));
			return personResource;
		}
	}
}
