package com.integration.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.person.Person;
import com.person.PersonRepository;
import com.person.subject.Degree;
import com.person.subject.Subject;
import com.person.subject.SubjectRepository;

public abstract class PersonRootControllerTest extends AbstractControllerTest{
	
	@Autowired
	protected PersonRepository personRepository;
	
	@Autowired
	protected SubjectRepository subjectRepository;
	
	protected long firstPersonId;
	protected long secondPersonId;
	protected long notExistingPersonId = -1;
	
	public void setUp() {
		super.setUp();
		cleanUpDatabase();
		insertSampleData();
		
		List<Person> persons = personRepository.findAll();
		firstPersonId = persons.get(0).getId();
		secondPersonId = persons.get(1).getId();
	}
	
	// localhost:8080/persons
	protected String getPersonCollectionLink() {
		return getHost() + "/persons";
	}

	// localhost:8080/persons/{personId}
	protected String getConcretePersonLink(long personId) {
		return getPersonCollectionLink() + "/" + personId;
	}

	// localhost:8080/persons/{personId}/subjects
	protected String getSubjectCollectionLinkForChosenPerson(long personId) {
		return getConcretePersonLink(personId) + "/subjects";
	}
	
	// localhost:8080/persons/{personId}/subjects/{subjectName}
	protected String getConcreteSubjectLinkForChosenPerson(Long personId, String subjectName) {
		return getSubjectCollectionLinkForChosenPerson(personId) + "/" + subjectName;
	}
	
	private void cleanUpDatabase() {
		personRepository.deleteAll();
		subjectRepository.deleteAll();
	}
	
	private void insertSampleData() {
		Subject subject = new Subject("Math",new ArrayList<>(Arrays.asList(Degree.THREE, Degree.THREE_AND_HALF, Degree.THREE_AND_HALF)));
		Subject subject2 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.FOUR, Degree.FOUR_AND_HALF, Degree.FIVE)));
		Subject subject3 = new Subject("English",new ArrayList<>(Arrays.asList(Degree.TWO, Degree.THREE, Degree.THREE)));
		
		Person person = new Person("Jan", "Nowak");
		Person person2 = new Person("Marcin", "Kowalski");
		
		person.addSubjects(subject);
		person.addSubjects(subject2);
		person2.addSubjects(subject3);
		
		personRepository.save(person);
		personRepository.save(person2);
	}
}
