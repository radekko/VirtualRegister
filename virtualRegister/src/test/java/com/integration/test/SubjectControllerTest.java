package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.response.Response;

import entities.Subject;

public class SubjectControllerTest extends StudentRootControllerTest{

	private final String PATH_TO_SUBJECT_COLLECTION = "_embedded.subjects[%1$s].";
	private final String LINK_TO_ROOT_SUBJECTS = LINKS+"subjectsForStudent.href";
	private final String NOT_EXISTING_SUBJECT_NAME = "not existing";
	private final String SEARCH_All_DEGREES = "marks. findAll {it}.value";

	private String subjectName;
	private static float markToAdd = 3.0f;
	private float notExistingMark = 2.5f;
	
	private final String ENTITY_NOT_EXIST_TEMPLATE = "Entity '%d' does not exist";
	private final String MESSAGE_IF_PERSON_NOT_EXIST = String.format(ENTITY_NOT_EXIST_TEMPLATE, notExistingStudentId);
	private final String DEGREE_IS_NOT_VALID_TEMPLATE = "Mark '%f' is not valid";
	private final String MESSAGE_IF_DEGREE_IS_OUT_OF_RANGE = String.format(DEGREE_IS_NOT_VALID_TEMPLATE, notExistingMark);
	
	@Before
	public void setUp() {
		super.setUp();
		Set<Subject> sortedSubjects = new TreeSet<>(subjectRepository.findByStudentId(firstStudentId));
		subjectName = sortedSubjects.iterator().next().getSubjectDetails().getSubjectName();
	}
	
	@After
	public void after() {
		super.after();
	}
	
	@Test
	public void getAllSubjectsForChosenStudent() {
		when()
			.get("/students/{id}/subjects",firstStudentId)
		.then()
			.statusCode(OK)
			.contentType(JSON)

		.root(getSubjectRootIfEmbedded(0))
			.body("subjectName", equalTo("English"))
			.body("ects", equalTo(6))
			.body("marks", hasSize(3))
			.body(SEARCH_All_DEGREES, contains(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"English")))
			
		.root(getSubjectRootIfEmbedded(1))
			.body("subjectName", equalTo("Math"))
			.body("ects", equalTo(5))
			.body("marks", hasSize(3))
			.body(SEARCH_All_DEGREES, contains(3.0f,3.5f,3.5f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"Math")))

		.noRoot()
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenStudent(firstStudentId)));
	}
	
	@Test
	public void getConcreteSubjectForChosenStudent() {
		when()
			.get("/students/{id}/subjects/{subjectName}",firstStudentId,subjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("ects", equalTo(6))
			.body("marks", hasSize(3))
			.body(SEARCH_All_DEGREES, contains(4.0f,4.5f,5.0f))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenStudent(firstStudentId)));
	}

	@Test
	public void getNotExistingSubjectForChosenStudent() {
		when()
			.get("/students/{id}/subjects/{subjectName}",firstStudentId,NOT_EXISTING_SUBJECT_NAME)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON);
	}
	
	@Test
	public void addMarksToChosenStudent() {
		Response res = sendMark(firstStudentId,subjectName,NO_CONTENT, markToAdd);
		isResponseBodyEmpty(res);

		when()
			.get("/students/{id}/subjects/{subjectName}",firstStudentId,subjectName)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("ects", equalTo(6))
			.body("marks", hasSize(4))
			.body(SEARCH_All_DEGREES, hasItems(4.0f,4.5f,5.0f,markToAdd))
			.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"English")))
			.body(LINK_TO_ROOT_SUBJECTS, equalTo(getSubjectCollectionLinkForChosenStudent(firstStudentId)));
	}

	@Test
	public void addMarksToNotExistingStudent() {
		Response res = sendMark(notExistingStudentId,NOT_EXISTING_SUBJECT_NAME,NOT_FOUND, markToAdd);
		isResponseBodyEmpty(res);
		
		assertThat(res.jsonPath().get("message"), equalTo(MESSAGE_IF_PERSON_NOT_EXIST));
	}
	
	@Test
	public void addAsideFromRangeMark() {
		Response res = sendMark(firstStudentId,subjectName,BAD_REQUEST, notExistingMark );
		isResponseBodyEmpty(res);
		
		assertThat(res.jsonPath().get("message"), equalTo(MESSAGE_IF_DEGREE_IS_OUT_OF_RANGE));
	}
	
	private Response sendMark(long studentId, String subjectName, int statusCode, float markToAdd) {
		return given()
			.contentType(JSON).body(markToAdd)
		.when()
			.put("/students/{id}/subjects/{subjectName}", studentId,subjectName)
		.then()
			.statusCode(statusCode)
		.extract()
			.response();
	}
	
	// _embedded.subjectResources[%1$s].
	private String getSubjectRootIfEmbedded(long subjectId) {
		return String.format(PATH_TO_SUBJECT_COLLECTION,subjectId);
	}
	
}