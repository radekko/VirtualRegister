package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.person.Person;
import com.person.subject.Degree;
import com.person.subject.Subject;

public class SubjectControllerTest extends PersonRootControllerTest{

	public final String PATH_TO_SUBJECT_COLLECTION = "_embedded.subjectResources[%1$s].";
	public final String LINK_TO_ROOT_SUBJECTS = LINKS+"subjectsForPerson.href";

	private long personId;
	private Person firstInsertedPerson;
	private String firstPersonFirstSubjectName;
	
	private static Degree degree;
	
	@BeforeClass
	public static void beforeAll() {
		degree = new Degree(3.0);
	}
	
	@Before
	public void setUp() {
		super.setUp();
		List<Person> persons = personRepository.findAll();
		firstInsertedPerson = persons.get(0);
		personId = firstInsertedPerson.getId();
		
		Set<Subject> sortedSubjects = new TreeSet<>(subjectRepository.findByPersonId(personId));
		firstPersonFirstSubjectName = sortedSubjects.iterator().next().getSubjectName();
	}
	
	@Test
	public void getAllSubjectsForChosenPerson() {
		when()
			.get("/persons/{id}/subjects",personId)
		.then()
			.statusCode(OK)
			.contentType(JSON)

		.root(getSubjectRootIfEmbedded(0))
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(personId,"English")))
		
		.root(getSubjectRootIfEmbedded(1))
			.body("subjectName", equalTo("Math"))
			.body("degree", hasItems(3.0f,3.5f,3.5f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(personId,"Math")))

		.noRoot()
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(personId)));
	}
	
	@Test
	public void getConcreteSubjectForChosenPerson() {
		when()
			.get("/persons/{id}/subjects/{subjectName}",personId,firstPersonFirstSubjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(personId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(personId)));
	}
	
	@Test
	public void addDegreesToChosenPerson() {
		Response res = sendDegree();
		isResponseBodyEmpty(res);

		verifyRecentlyAddedDegree(degree);
	}

	private Response sendDegree() {
		return given()
			.contentType(JSON).body(degree)
		.when()
			.put("/persons/{id}/subjects/{subjectName}", personId,firstPersonFirstSubjectName)
		.then()
			.statusCode(NO_CONTENT)
		.extract()
			.response();
	}
	
	private void verifyRecentlyAddedDegree(Degree degree) {
		when()
			.get("/persons/{id}/subjects/{subjectName}",personId,firstPersonFirstSubjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f,(float)degree.getDegree()))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(personId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(personId)));
	}

	//localhost:8080/persons/{personId}/subjects/{subjectName}
	private String getConcreteSubjectLinkForChosenPerson(Long personId, String subjectName) {
		return getSubjectCollectionLinkForChosenPerson(personId) + "/" +subjectName;
	}
	
	//localhost:8080/persons/{personId}/subjects
	private String getSubjectCollectionLinkForChosenPerson(Long personId) {
		return getHost() + "/persons/" + personId + "/subjects";
	}
	
	//_embedded.subjectResources[%1$s].
	private String getSubjectRootIfEmbedded(long subjectId) {
		return String.format(PATH_TO_SUBJECT_COLLECTION,subjectId);
	}
	
}
