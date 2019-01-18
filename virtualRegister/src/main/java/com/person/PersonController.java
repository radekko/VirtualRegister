package com.person;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core.EntityNotExistException;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	
	private final PersonsResourceAssembler personsResource;
	private final PersonRepository personRepository;
	

	public PersonController(PersonsResourceAssembler personsResource, PersonRepository personRepository) {
		this.personsResource = personsResource;
		this.personRepository = personRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<PersonResource>> getAllPersons() {
		Resources<PersonResource> resources = personsResource.toGlobalResource(personRepository.findAll());
		return ResponseEntity.ok().body(resources);
	}
	
	@GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<PersonResource> getPerson(@PathVariable Long id) throws EntityNotExistException {
		PersonResource pr = personRepository.findById(id)
				.map(personsResource::toResource)
				.orElseThrow(() -> new EntityNotExistException(id));
		
		return ResponseEntity.ok().body(pr);
	}
	
	@PostMapping
	public HttpEntity<Void> newEmployee(@RequestBody NewPerson newPerson) throws URISyntaxException {
		Person p = personRepository.save(new Person(newPerson.getFirstName(),newPerson.getLastName()));
		PersonResource res = personsResource.toResource(p);
		return ResponseEntity.created(new URI(res.getId().expand().getHref())).build();
	}
		
	@PutMapping(value="/{id}")
	public HttpEntity<Void> replacePerson(@RequestBody NewPerson newPerson, @PathVariable Long id) throws EntityNotExistException {
		personRepository.findById(id)
						.map(updatePerson(newPerson)
						.andThen(storePerson()))
						.orElseThrow(() -> new EntityNotExistException(id));
		
		return ResponseEntity.noContent().build();
	}
	
	private Function<Person, Person> updatePerson(NewPerson newPerson) {
		return p -> {
			p.setFirstName(newPerson.getFirstName());
			p.setLastName(newPerson.getLastName());
			return p;
		};
	}
	
	private Function<Person, Person> storePerson() {
		return personRepository :: save;
	}
	
}
