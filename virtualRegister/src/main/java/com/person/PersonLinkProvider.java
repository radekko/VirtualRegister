package com.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.exceptions.EntityNotExistException;

@Component
public class PersonLinkProvider {
	
	public List<Link> getLinksForCollection(){
		List<Link> links = new ArrayList<>();
		links.add(linkToGlobalCollection());
		return links;
	}
	
	public List<Link> getLinksForEmbeddedPerson(Person person){
		List<Link> links = new ArrayList<>();
		links.add(linkToSelf(person));
		return links;
	}
	
	public List<Link> getLinksForChosenPerson(Person person){
		List<Link> links = new ArrayList<>();
		links.add(linkToSelf(person));
		links.add(linkToGlobalCollection());
		return links;
	}
	
	private Link linkToGlobalCollection() {
		return linkTo(methodOn(PersonController.class).getAllPersons()).withRel("persons");
	}
	
	private Link linkToSelf(Person person){
		Link link;
		try {link = linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel();}
		catch (EntityNotExistException e) {throw new RuntimeException("Person not exist");}
		return link;
	}
}
