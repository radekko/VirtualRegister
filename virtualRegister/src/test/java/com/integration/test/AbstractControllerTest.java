package com.integration.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.core.Application;
import com.jayway.restassured.RestAssured;
import com.person.Person;
import com.person.PersonRepository;
import com.person.subject.Subject;
import com.person.subject.SubjectRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

	@LocalServerPort
	int port;
	
	@Autowired
	protected PersonRepository personRepository;
	
	@Autowired
	protected SubjectRepository subjectRepository;

	public void setUp() {
		RestAssured.port = port;
		insertSampleData();
	}
	
	protected String getHost() {
		return "http://localhost:"+port;
	}

	private void insertSampleData() {
		personRepository.deleteAll();
		subjectRepository.deleteAll();
		
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
