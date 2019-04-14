package com.integration.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.person.Person;
import com.person.PersonRepository;
import com.person.subject.Subject;
import com.person.subject.SubjectRepository;

public abstract class PersonRootControllerTest extends AbstractControllerTest{
	
	@Autowired
	public PersonRepository personRepository;
	
	@Autowired
	public SubjectRepository subjectRepository;
	
	public void setUp() {
		super.setUp();
		cleanUpDatabase();
		insertSampleData();
	}
	
	private void cleanUpDatabase() {
		personRepository.deleteAll();
		subjectRepository.deleteAll();
	}
	
	private void insertSampleData() {
		Subject subject = new Subject("Math",new ArrayList<>(Arrays.asList(3.0, 3.5, 3.5)));
		Subject subject2 = new Subject("English",new ArrayList<>(Arrays.asList(4.0, 4.5, 5.0)));
		Subject subject3 = new Subject("English",new ArrayList<>(Arrays.asList(2.0, 2.5, 3.0)));
		
		Person person = new Person("Jan", "Nowak");
		Person person2 = new Person("Marcin", "Kowalski");
		
		person.addSubjects(subject);
		person.addSubjects(subject2);
		person2.addSubjects(subject3);
		
		personRepository.save(person);
		personRepository.save(person2);
	}
}
