package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.RestAssured.withArgs;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.student.NewStudent;
import com.student.subject.NewSubjectDetails;

public class StudentControllerTest extends StudentRootControllerTest {
	
	private final String PATH_TO_PERSON_COLLECTION = "_embedded.students[%1$s].";
	private final String LINK_TO_PERSONS_COLLECTION = LINKS + "students.href";
	private final String LINK_TO_SUBJECT_COLLECTION_IN_PERSONS = LINKS + "subjectsForStudent.href"; 
	private final String LINK_TO_SUBJECT_COLLECTION = "_embedded.studentSubjects." + LINK_TO_SUBJECT_COLLECTION_IN_PERSONS; 
	private final String PATH_TO_SUBJECTS = "_embedded.studentSubjects._embedded.subjects.";
	
	private final String PATH_TO_SUBJECT_NAME = PATH_TO_SUBJECTS + "subjectName";
	private final String SEARCH_BY_SUBJECT_ROOT_PATH = PATH_TO_SUBJECTS + "find {it.subjectName == '%s'}";
	private final String SEARCH_All_DEGREES = "marks. findAll {it}.value";
	
	private final String MESSAGE_FORMAT = "Entity '%d' does not exist";
	private final String MESSAGE_IF_PERSON_NOT_EXIST = String.format(MESSAGE_FORMAT, notExistingStudentId);
	
	private static final String MESSAGE_FORMAT_STRING = "Entity '%s' does not exist";
	private final String MESSAGE_IF_SUBJECT_DETAILS_NOT_EXIST = String.format(MESSAGE_FORMAT_STRING, notExistingSubjectName);
	
	private static final String MESSAGE_FORMAT_STRING_2 = "Entity '%s' in this student already exist";
	private final String MESSAGE_IF_SUBJECT_ALREADY_EXIST_IN_PERSON = String.format(MESSAGE_FORMAT_STRING_2, alreadyExistSubjectName);
	
	private static NewStudent newStudent;
	private static NewSubjectDetails newSubject;
	private static String notAssignedSubjectNameToAnyPerson = "Chemistry";
	
	@BeforeClass
	public static void beforeAll() {
		newStudent = new NewStudent("Marcin","Kot");
		newSubject = new NewSubjectDetails("Physics",7);
	}
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void getAllStudents() {
		when()
			.get("/students")
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.root(getStudentRootIfEmbedded(0))
				.body("firstName", equalTo("Jan"))
				.body("lastName", equalTo("Nowak"))
				.body(LINK_TO_SUBJECT_COLLECTION_IN_PERSONS, equalTo(getSubjectCollectionLinkForChosenStudent(firstStudentId)))    // http://localhost:8080/students/1/subjects
				.body(LINK_TO_SELF, equalTo(getConcreteStudentLink(firstStudentId)))
			.root(getStudentRootIfEmbedded(1))
				.body("firstName", equalTo("Marcin"))
				.body("lastName", equalTo("Kowalski"))
				.body(LINK_TO_SUBJECT_COLLECTION_IN_PERSONS, equalTo(getSubjectCollectionLinkForChosenStudent(secondStudentId)))    // http://localhost:8080/students/2/subjects
				.body(LINK_TO_SELF, equalTo(getConcreteStudentLink(secondStudentId)))
			.noRoot()
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getStudentCollectionLink()));
	}
	
	@Test
	public void getStudent() {
		when()
			.get("/students/{id}",firstStudentId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo("Jan"))
			.body("lastName", equalTo("Nowak"))
		
			.root(SEARCH_BY_SUBJECT_ROOT_PATH,withArgs("English"))
				.body(SEARCH_All_DEGREES, contains(4.0f,4.5f,5.0f))
				.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"English")))  // http://localhost:8080/students/1/subjects/English
		
			.root(SEARCH_BY_SUBJECT_ROOT_PATH,withArgs("Math"))
				.body(SEARCH_All_DEGREES, contains(3.0f,3.5f,3.5f))
				.body(LINK_TO_SELF, equalTo(getConcreteSubjectLinkForChosenStudent(firstStudentId,"Math")))  // http://localhost:8080/students/1/subjects/Math
		
			.noRoot()
				.body(LINK_TO_SELF, equalTo(getConcreteStudentLink(firstStudentId))) 						 // http://localhost:8080/students/1
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getStudentCollectionLink()))					 // http://localhost:8080/students
				.body(LINK_TO_SUBJECT_COLLECTION, equalTo(getSubjectCollectionLinkForChosenStudent(firstStudentId)));    // http://localhost:8080/students/1/subjects
	}
	
	@Test
	public void getNotExistingStudent() {
		when()
			.get("/students/{id}",notExistingStudentId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_PERSON_NOT_EXIST));
	}
	
	@Test
	public void createNewStudent(){
		Response response = given()
				.contentType(JSON).body(newStudent)
		   .when()
		   		.post("/students")
		   	.then()
		   		.statusCode(CREATED)
			.extract()
				.response();
		isResponseBodyEmpty(response);

		String newStudentLocation = response.getHeader("location");
		when()
			.get(newStudentLocation)
		.then()
			.statusCode(OK)
			.contentType(JSON)
				.body("firstName", equalTo(newStudent.getFirstName()))
				.body("lastName", equalTo(newStudent.getLastName()))
				.body(LINK_TO_SELF, equalTo(newStudentLocation)) 						 // http://localhost:8080/students/{newStudentLocation}
				.body(LINK_TO_PERSONS_COLLECTION, equalTo(getStudentCollectionLink()));  // http://localhost:8080/students
	}

	@Test
	public void addExistingSubjectToStudent() {
		Response response = given()
			.contentType(JSON).body(notAssignedSubjectNameToAnyPerson)
		.when()
			.put("/students/{id}/subjects", firstStudentId)
		.then()
			.statusCode(NO_CONTENT)
		.extract()
			.response();
		isResponseBodyEmpty(response);
		
		when()
			.get("/students/{id}",firstStudentId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo("Jan"))
			.body("lastName", equalTo("Nowak"))
			.body(PATH_TO_SUBJECT_NAME, hasItems("Math","English","Chemistry"))
			.body(LINK_TO_SELF, equalTo(getConcreteStudentLink(firstStudentId)))
			.body(LINK_TO_PERSONS_COLLECTION, equalTo(getStudentCollectionLink()));
	}
	
	@Test
	public void addNotExistingSubjectToStudent() {
		given()
			.contentType(JSON).body(notExistingSubjectName)
		.when()
			.put("/students/{id}/subjects", firstStudentId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_SUBJECT_DETAILS_NOT_EXIST));
	}
	
	@Test
	public void addAlreadyExistingSubjectInStudent() {
		given()
			.contentType(JSON).body("Math")
		.when()
			.put("/students/{id}/subjects", firstStudentId)
		.then()
			.statusCode(CONFLICT)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_SUBJECT_ALREADY_EXIST_IN_PERSON));
	}
	
	@Test
	public void addSubjectToNotExistingStudent() {
		given()
			.contentType(JSON).body(newSubject)
		.when()
			.put("/students/{id}/subjects", notExistingStudentId)
		.then()
			.statusCode(NOT_FOUND)
			.contentType(JSON)
			.body("message",equalTo(MESSAGE_IF_PERSON_NOT_EXIST));
	}

	@Test
	public void renameStudent() {
		Response res = verifyChangeStudentDetails(newStudent,firstStudentId,NO_CONTENT);
		isResponseBodyEmpty(res);
		
		when()
			.get("/students/{id}",firstStudentId)
		.then()
			.statusCode(OK)
			.contentType(JSON)
			.body("firstName", equalTo(newStudent.getFirstName()))
			.body("lastName", equalTo(newStudent.getLastName()))
			.body(PATH_TO_SUBJECT_NAME, hasItems("Math","English"))
			.body(LINK_TO_SELF, equalTo(getConcreteStudentLink(firstStudentId)))
			.body(LINK_TO_PERSONS_COLLECTION, equalTo(getStudentCollectionLink()));
	}
	
	@Test
	public void renameNotExistingStudent() {
		NewStudent newStudent = new NewStudent("Marcin","Kot");
		
		Response res = verifyChangeStudentDetails(newStudent,notExistingStudentId,NOT_FOUND);
		isResponseBodyEmpty(res);
	}

	private Response verifyChangeStudentDetails(NewStudent newStudent, Long studentId, int expectedStatusCode) {
		return given()
					.contentType(JSON).body(newStudent)
				.when()
					.put("/students/{id}", studentId)
				.then()
					.statusCode(expectedStatusCode)
				.extract()
					.response();
	}

	// _embedded.studentResources[{studentId}].
	private String getStudentRootIfEmbedded(long studentId) {
		return String.format(PATH_TO_PERSON_COLLECTION,studentId);
	}
}