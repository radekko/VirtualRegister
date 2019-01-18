package com.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component 
public class PersonsResourceAssembler {
	
	private final PersonResourceAssembler pra;
	
	public PersonsResourceAssembler(PersonResourceAssembler pra) {
		this.pra = pra;
	}

	public Resources<PersonResource> toGlobalResource(List<Person> list) {
		List<PersonResource> personResources = list.stream().map(pra::toResource).collect(Collectors.toList());
		
		Resources<PersonResource> resources = new Resources<>(personResources);
		Link selfLink = linkTo(methodOn(PersonController.class).getAllPersons()).withSelfRel();
		resources.add(selfLink);
		return resources;
	}
	
	public PersonResource toResource(Person person) {
		return pra.toResource(person);
	}
	
}
