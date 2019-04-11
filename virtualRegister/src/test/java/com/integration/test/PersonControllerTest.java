package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.RestAssured.withArgs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jayway.restassured.http.ContentType.JSON;
import com.jayway.restassured.response.Response;
import com.person.NewPerson;
import com.person.Person;
import com.person.PersonRepository;
import com.person.subject.NewSubject;
import com.person.subject.Subject;
import com.person.subject.SubjectRepository;

public class PersonControllerTest extends AbstractControllerTest {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	private String linkToPersonCollection = "_embedded.personResources[%1$s].";
	private String links = "_links.";
	private String linkToSelf = links+"self.href";
	private String linkToPersonsCollection = links+"persons.href";
	private String linkToSubjectCollection = "subjectResource."+links+"subjectsForPerson.href"; 
	
	private String linkToSubjectName = "subjectResource._embedded.subjectResources.subjectName";
	private String searchBySubjectRootPath = "subjectResource._embedded.subjectResources.find {it.subjectName == '%s'}";
	private long firstPersonId;
	private long secondPersonId;
	private long notExistingPersonId;
	
	private static final String MESSAGE_FORMAT = "Entity '%d' does not exist";
	private String messageIfPersonNotExist;
	
	@Before
	public void setUp() {
		super.setUp();
		insertSampleData();
		List<Person> persons = personRepository.findAll();
		firstPersonId = persons.get(0).getId();
		secondPersonId = persons.get(1).getId();
		notExistingPersonId = 100;
		messageIfPersonNotExist = String.format(MESSAGE_FORMAT, notExistingPersonId);
	}
	
	@After
	public void cleanUpDatabase() {
		personRepository.deleteAll();
		subjectRepository.deleteAll();
	}
	
	@Test
	public void getAllPersons() {
		when()
			.get("/persons")
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.root(getPersonRoot(0))
				.body("firstName", equalTo("Jan"))
				.body("lastName", equalTo("Nowak"))
				.body(linkToSubjectName, hasItems("Math","English"))
				.body(linkToSelf, equalTo(getPersonSingleLink(firstPersonId)))
			.root(getPersonRoot(1))
				.body("firstName", equalTo("Marcin"))
				.body("lastName", equalTo("Kowalski"))
				.body(linkToSubjectName, hasItems("English"))
				.body(linkToSelf, equalTo(getPersonSingleLink(secondPersonId)))
			.noRoot()
				.body(linkToPersonsCollection, equalTo(getPersonCollectionLink()));
	}
	
	@Test
	public void getPerson() {
		when()
			.get("/persons/{id}",firstPersonId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo("Jan"))
			.body("lastName", equalTo("Nowak"))
		
			.root(searchBySubjectRootPath,withArgs("English"))
				.body("degree", hasItems(4.0f,4.5f,5.0f))
				.body(linkToSelf, equalTo(getSubjectSingleLink(firstPersonId,"English")))        // http://localhost:8080/persons/1/subjects/English
		
			.root(searchBySubjectRootPath,withArgs("Math"))
				.body("degree", hasItems(3.0f,3.5f,3.5f))
				.body(linkToSelf, equalTo(getSubjectSingleLink(firstPersonId,"Math")))            // http://localhost:8080/persons/1/subjects/Math
		
			.noRoot()
				.body(linkToSelf, equalTo(getPersonSingleLink(firstPersonId))) 						 // http://localhost:8080/persons/1
				.body(linkToPersonsCollection, equalTo(getPersonCollectionLink()))					 // http://localhost:8080/persons
				.body(linkToSubjectCollection, equalTo(getSubjectCollectionLink(firstPersonId)));    // http://localhost:8080/persons/1/subjects
	}
	
	@Test
	public void getNotExistingPerson() {
		when()
			.get("/persons/{id}",notExistingPersonId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(messageIfPersonNotExist));
	}
	
	@Test
	public void createNewPerson(){
		NewPerson newPerson = new NewPerson("Marcin", "Kot");
		
		Response response = verifyPostNewPerson(newPerson);
		isResponseBodyEmpty(response);

		String newPersonLocation = response.getHeader("location");
		verifyRecentlyCreatedPerson(newPerson, newPersonLocation);
	}

	private Response verifyPostNewPerson(NewPerson newPerson) {
		return given()
					.contentType(JSON).body(newPerson)
			   .when()
			   		.post("/persons")
			   	.then()
			   		.statusCode(CREATED)
				.extract()
					.response();
	}
	
	private void verifyRecentlyCreatedPerson(NewPerson newPerson, String newPersonLocation) {
		when()
			.get(newPersonLocation)
		.then()
			.statusCode(OK)
			.contentType(JSON)
				.body("firstName", equalTo(newPerson.getFirstName()))
				.body("lastName", equalTo(newPerson.getLastName()))
				.body(linkToSelf, equalTo(newPersonLocation)) 						 // http://localhost:8080/persons/{newPersonLocation}
				.body(linkToPersonsCollection, equalTo(getPersonCollectionLink()));  // http://localhost:8080/persons
	}

	@Test
	public void addSubjectToPerson() {
		NewSubject newSubject = new NewSubject("Physics");
		
		Response response = verifyPutNewSubject(newSubject);
		isResponseBodyEmpty(response);
		
		verifyRecentlyPutSubject();
	}
	
	@Test
	public void addSubjectToNotExistingPerson() {
		NewSubject newSubject = new NewSubject("Physics");

		given()
			.contentType(JSON).body(newSubject).param("personId",notExistingPersonId)
		.when()
			.put("/persons/{id}/subjects", notExistingPersonId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(messageIfPersonNotExist));
	}

	private Response verifyPutNewSubject(NewSubject newSubject) {
		return given()
					.contentType(JSON).body(newSubject).param("personId",firstPersonId)
				.when()
					.put("/persons/{id}/subjects", firstPersonId)
				.then()
					.statusCode(NO_CONTENT)
				.extract()
					.response();
	}

	private void verifyRecentlyPutSubject() {
		when()
			.get("/persons/{id}",firstPersonId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo("Jan"))
			.body("lastName", equalTo("Nowak"))
			.body(linkToSubjectName, hasItems("Math","English","Physics"))
			.body(linkToSelf, equalTo(getPersonSingleLink(firstPersonId)))
			.body(linkToPersonsCollection, equalTo(getPersonCollectionLink()));
	}

	@Test
	public void renamePerson() {
		NewPerson newPerson = new NewPerson("Marcin","Kot");
		
		Response res = verifyChangePersonDetails(newPerson);
		isResponseBodyEmpty(res);

		verifyRecentlyChangedPerson(newPerson);
	}

	private Response verifyChangePersonDetails(NewPerson newPerson) {
		return given()
					.contentType(JSON).body(newPerson).param("personId",firstPersonId)
				.when()
					.put("/persons/{id}", firstPersonId)
				.then()
					.statusCode(NO_CONTENT)
				.extract()
					.response();
	}

	private void verifyRecentlyChangedPerson(NewPerson newPerson) {
		when()
			.get("/persons/{id}",firstPersonId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo(newPerson.getFirstName()))
			.body("lastName", equalTo(newPerson.getLastName()))
			.body(linkToSubjectName, hasItems("Math","English"))
			.body(linkToSelf, equalTo(getPersonSingleLink(firstPersonId)))
			.body(linkToPersonsCollection, equalTo(getPersonCollectionLink()));
	}
	
	private void isResponseBodyEmpty(Response res) {
		assertThat(res.getHeader("Content-Lenght"), nullValue());
	}
	
	private String getPersonRoot(long personId) {
		return String.format(linkToPersonCollection,personId);
	}
	
	private String getPersonSingleLink(long resourceId) {
		return getPersonCollectionLink() + "/" + resourceId;
	}
	
	private String getSubjectSingleLink(long resourceId, String subjectName) {
		return getSubjectCollectionLink(resourceId) + "/" + subjectName;
	}
	
	private String getSubjectCollectionLink(long resourceId) {
		return getPersonCollectionLink() + "/" + resourceId + "/subjects";
	}
	
	private String getPersonCollectionLink() {
		return getHost() + "/persons";
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
