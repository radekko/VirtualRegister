package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.RestAssured.withArgs;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.person.NewPerson;
import com.person.Person;
import com.person.subject.NewSubject;

public class PersonControllerTest extends PersonRootControllerTest {
	
	public final String PATH_TO_PERSON_COLLECTION = "_embedded.personResources[%1$s].";
	public final String LINK_TO_PERSONS_COLLECTION = LINKS+"persons.href";
	public final String LINK_TO_SUBJECT_COLLECTION = "subjectResource."+LINKS+"subjectsForPerson.href"; 
	
	public final String PATH_TO_SUBJECTS = "subjectResource._embedded.subjectResources.";
	public final String PATH_TO_SUBJECT_TO_SUBJECT_NAME = PATH_TO_SUBJECTS + "subjectName";
	public final String SEARCH_BY_SUBJECT_ROOT_PATH = PATH_TO_SUBJECTS + "find {it.subjectName == '%s'}";
	
	private long firstPersonId;
	private long secondPersonId;
	private long notExistingPersonId = 100;
	
	private static final String MESSAGE_FORMAT = "Entity '%d' does not exist";
	private String MESSAGE_IF_PERSON_NOT_EXIST = String.format(MESSAGE_FORMAT, notExistingPersonId);
	
	private static NewPerson newPerson;
	private static NewSubject newSubject;
	
	@BeforeClass
	public static void beforeAll() {
		newPerson = new NewPerson("Marcin","Kot");
		newSubject = new NewSubject("Physics");
	}
	
	@Before
	public void setUp() {
		super.setUp();
		List<Person> persons = personRepository.findAll();
		firstPersonId = persons.get(0).getId();
		secondPersonId = persons.get(1).getId();
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
				.body(PATH_TO_SUBJECT_TO_SUBJECT_NAME, hasItems("Math","English"))
				.body(LINK_TO_SELF, equalTo(getPersonSingleLink(firstPersonId)))
			.root(getPersonRoot(1))
				.body("firstName", equalTo("Marcin"))
				.body("lastName", equalTo("Kowalski"))
				.body(PATH_TO_SUBJECT_TO_SUBJECT_NAME, hasItems("English"))
				.body(LINK_TO_SELF, equalTo(getPersonSingleLink(secondPersonId)))
			.noRoot()
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getPersonCollectionLink()));
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
		
			.root(SEARCH_BY_SUBJECT_ROOT_PATH,withArgs("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f))
				.body(LINK_TO_SELF, equalTo(getSubjectSingleLink(firstPersonId,"English")))        // http://localhost:8080/persons/1/subjects/English
		
			.root(SEARCH_BY_SUBJECT_ROOT_PATH,withArgs("Math"))
				.body("degree", hasItems(3.0f,3.5f,3.5f))
				.body(LINK_TO_SELF, equalTo(getSubjectSingleLink(firstPersonId,"Math")))            // http://localhost:8080/persons/1/subjects/Math
		
			.noRoot()
				.body(LINK_TO_SELF, equalTo(getPersonSingleLink(firstPersonId))) 						 // http://localhost:8080/persons/1
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getPersonCollectionLink()))					 // http://localhost:8080/persons
				.body(LINK_TO_SUBJECT_COLLECTION, equalTo(getSubjectCollectionLink(firstPersonId)));    // http://localhost:8080/persons/1/subjects
	}
	
	@Test
	public void getNotExistingPerson() {
		when()
			.get("/persons/{id}",notExistingPersonId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_PERSON_NOT_EXIST));
	}
	
	@Test
	public void createNewPerson(){
		Response response = verifyPostNewPerson();
		isResponseBodyEmpty(response);

		String newPersonLocation = response.getHeader("location");
		verifyRecentlyCreatedPerson(newPerson, newPersonLocation);
	}

	private Response verifyPostNewPerson() {
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
				.body(LINK_TO_SELF, equalTo(newPersonLocation)) 						 // http://localhost:8080/persons/{newPersonLocation}
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getPersonCollectionLink()));  // http://localhost:8080/persons
	}

	@Test
	public void addSubjectToPerson() {
		Response response = verifyPutNewSubject();
		isResponseBodyEmpty(response);
		
		verifyRecentlyPutSubject();
	}
	
	@Test
	public void addSubjectToNotExistingPerson() {
		given()
			.contentType(JSON).body(newSubject)
		.when()
			.put("/persons/{id}/subjects", notExistingPersonId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_PERSON_NOT_EXIST));
	}

	private Response verifyPutNewSubject() {
		return given()
					.contentType(JSON).body(newSubject)
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
			.body(PATH_TO_SUBJECT_TO_SUBJECT_NAME, hasItems("Math","English","Physics"))
			.body(LINK_TO_SELF, equalTo(getPersonSingleLink(firstPersonId)))
			.body(LINK_TO_PERSONS_COLLECTION, equalTo(getPersonCollectionLink()));
	}

	@Test
	public void renamePerson() {
		Response res = verifyChangePersonDetails(newPerson,firstPersonId,NO_CONTENT);
		isResponseBodyEmpty(res);
		
		verifyRecentlyChangedPerson(newPerson);
	}
	
	@Test
	public void renameNotExistingPerson() {
		NewPerson newPerson = new NewPerson("Marcin","Kot");
		
		Response res = verifyChangePersonDetails(newPerson,notExistingPersonId,NOT_FOUND);
		isResponseBodyEmpty(res);
	}

	private Response verifyChangePersonDetails(NewPerson newPerson, Long personId, int expectedStatusCode) {
		return given()
					.contentType(JSON).body(newPerson)
				.when()
					.put("/persons/{id}", personId)
				.then()
					.statusCode(expectedStatusCode)
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
			.body(PATH_TO_SUBJECT_TO_SUBJECT_NAME, hasItems("Math","English"))
			.body(LINK_TO_SELF, equalTo(getPersonSingleLink(firstPersonId)))
			.body(LINK_TO_PERSONS_COLLECTION, equalTo(getPersonCollectionLink()));
	}
	
	private String getPersonRoot(long personId) {
		return String.format(PATH_TO_PERSON_COLLECTION,personId);
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

}
