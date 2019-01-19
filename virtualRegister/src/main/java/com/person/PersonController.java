package com.person;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

import javax.transaction.Transactional;

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
import com.person.subject.NewSubject;
import com.person.subject.Subject;

@RestController
@Transactional
@RequestMapping(value = "/persons")
public class PersonController {

	private final PersonsResourceAssembler personsResourceAssemb;
	private final PersonRepository personRepository;

	public PersonController(PersonsResourceAssembler personsResourceAssemb, PersonRepository personRepository) {
		this.personsResourceAssemb = personsResourceAssemb;
		this.personRepository = personRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<PersonResource>> getAllPersons() {
		Resources<PersonResource> resources = personsResourceAssemb.toGlobalResource(personRepository.findAll());
		return ResponseEntity.ok().body(resources);
	}
	
	@GetMapping(value="/{personId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<PersonResource> getPerson(@PathVariable Long personId) throws EntityNotExistException {
		PersonResource pr = personRepository.findById(personId)
				.map(personsResourceAssemb::toResource)
				.orElseThrow(() -> new EntityNotExistException(personId));
		
		return ResponseEntity.ok().body(pr);
	}
	
	@PostMapping
	public HttpEntity<Void> newEmployee(@RequestBody NewPerson newPerson) throws URISyntaxException {
		Person p = personRepository.save(new Person(newPerson.getFirstName(),newPerson.getLastName()));
		PersonResource res = personsResourceAssemb.toResource(p);
		return ResponseEntity.created(new URI(res.getId().expand().getHref())).build();
	}
	
	@PutMapping(value="/{personId}/subjects")
	public HttpEntity<Void> addSubjectToPerson(
			@PathVariable Long personId, @RequestBody NewSubject newSubject) throws EntityNotExistException{
		
		Person p = personRepository.findById(personId).orElseThrow(() -> new EntityNotExistException(personId));
		
		if(checkIfSubjectNotExist(p, newSubject))
			addSubjectToPersonAndStore(p, newSubject);
			
		return ResponseEntity.noContent().build();
	}

	private boolean checkIfSubjectNotExist(Person p, NewSubject newSubject) {
		return p.getSubjects().stream().noneMatch(s -> s.getSubjectName().equals(newSubject.getSubjectName()));
	}

	private void addSubjectToPersonAndStore(Person p, NewSubject newSubject) {
		p.addSubjects(new Subject(newSubject.getSubjectName()));
		personRepository.save(p);
	}

	@PutMapping(value="/{personId}")
	public HttpEntity<Void> replacePerson(@RequestBody NewPerson newPerson, @PathVariable Long personId) throws EntityNotExistException {
		personRepository.findById(personId)
						.map(updatePerson(newPerson)
						.andThen(storePerson()))
						.orElseThrow(() -> new EntityNotExistException(personId));
		
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