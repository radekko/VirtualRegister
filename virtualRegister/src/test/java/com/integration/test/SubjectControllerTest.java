package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.person.subject.Degree;
import com.person.subject.Subject;

public class SubjectControllerTest extends PersonRootControllerTest{

	private final String PATH_TO_SUBJECT_COLLECTION = "_embedded.subjectResources[%1$s].";
	private final String LINK_TO_ROOT_SUBJECTS = LINKS+"subjectsForPerson.href";
	private final String NOT_EXISTING_SUBJECT_NAME = "not existing";

	private String subjectName;
	private static Degree degree;
	
	@BeforeClass
	public static void beforeAll() {
		degree = new Degree(3.0);
	}
	
	@Before
	public void setUp() {
		super.setUp();
		
		Set<Subject> sortedSubjects = new TreeSet<>(subjectRepository.findByPersonId(firstPersonId));
		subjectName = sortedSubjects.iterator().next().getSubjectName();
	}
	
	@Test
	public void getAllSubjectsForChosenPerson() {
		when()
			.get("/persons/{id}/subjects",firstPersonId)
		.then()
			.statusCode(OK)
			.contentType(JSON)

		.root(getSubjectRootIfEmbedded(0))
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(firstPersonId,"English")))
		
		.root(getSubjectRootIfEmbedded(1))
			.body("subjectName", equalTo("Math"))
			.body("degree", hasItems(3.0f,3.5f,3.5f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(firstPersonId,"Math")))

		.noRoot()
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(firstPersonId)));
	}
	
	@Test
	public void getConcreteSubjectForChosenPerson() {
		when()
			.get("/persons/{id}/subjects/{subjectName}",firstPersonId,subjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(firstPersonId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(firstPersonId)));
	}

	@Test
	public void getNotExistingSubjectForChosenPerson() {
		when()
			.get("/persons/{id}/subjects/{subjectName}",firstPersonId,NOT_EXISTING_SUBJECT_NAME)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON);
	}
	
	@Test
	public void addDegreesToChosenPerson() {
		Response res = sendDegree(firstPersonId,subjectName,NO_CONTENT);
		isResponseBodyEmpty(res);

		verifyRecentlyAddedDegree(degree);
	}

	@Test
	public void addDegreesToNotExistingPerson() {
		Response res = sendDegree(notExistingPersonId,NOT_EXISTING_SUBJECT_NAME,NOT_FOUND);
		isResponseBodyEmpty(res);
	}
	
	private Response sendDegree(long personId, String subjectName, int statusCode) {
		return given()
			.contentType(JSON).body(degree)
		.when()
			.put("/persons/{id}/subjects/{subjectName}", personId,subjectName)
		.then()
			.statusCode(statusCode)
		.extract()
			.response();
	}
	
	private void verifyRecentlyAddedDegree(Degree degree) {
		when()
			.get("/persons/{id}/subjects/{subjectName}",firstPersonId,subjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("degree", hasItems(4.0f,4.5f,5.0f,(float)degree.getDegree()))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenPerson(firstPersonId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenPerson(firstPersonId)));
	}

	// _embedded.subjectResources[%1$s].
	private String getSubjectRootIfEmbedded(long subjectId) {
		return String.format(PATH_TO_SUBJECT_COLLECTION,subjectId);
	}
	
}