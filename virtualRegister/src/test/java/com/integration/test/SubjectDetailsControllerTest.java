package com.integration.test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.response.Response;
import com.student.subject.NewSubjectDetails;
import com.subjectDetails.SubjectDetails;
import com.subjectDetails.SubjectDetailsRepository;

public class SubjectDetailsControllerTest extends AbstractControllerTest{
	
	private final String PATH_TO_SUBJECT_DETAILS_COLLECTION = "_embedded.subjects_details[%1$s].";
	private final String LINK_TO_SUBJECT_DETAILS_COLLECTION = LINKS + "subjectDetails.href";

	@Autowired
	private SubjectDetailsRepository subjectDetailsRepository;
	
	private static NewSubjectDetails newSubjectDetails;
	
	@BeforeClass
	public static void beforeAll() {
		newSubjectDetails = new NewSubjectDetails("Biology",2);
	}
	
	@Before
	public void setUp() {
		super.setUp();
		cleanUpDatabase();
		insertSampleData();
	}
	
	@Test
	public void testGetAllSubjectsDetails() {
		when()
			.get("/subject_details")
		.then()
			.statusCode(OK)
			.contentType(JSON)
		.root(getSubjectDetailsRootIfEmbedded(0))
			.body("subjectName", equalTo("English"))
			.body("ects", equalTo(6))
			.body(LINK_TO_SELF, equalTo("http://localhost:"+port+"/subject_details/English"))
		
		.root(getSubjectDetailsRootIfEmbedded(1))
			.body("subjectName", equalTo("Math"))
			.body("ects", equalTo(5))
			.body(LINK_TO_SELF, equalTo("http://localhost:"+port+"/subject_details/Math"))
			
		.noRoot()
			.body(LINK_TO_SUBJECT_DETAILS_COLLECTION, equalTo("http://localhost:"+port+"/subject_details"));
	}
	
	@Test
	public void testGetSubjectDetails() {
		when()
			.get("/subject_details/{subjectName}","English")
		.then()
			.log()
			.ifValidationFails()
			.statusCode(OK)
			.contentType(JSON)
			.body("subjectName", equalTo("English"))
			.body("ects", equalTo(6))
			.body(LINK_TO_SELF, equalTo("http://localhost:"+port+"/subject_details/English"))
			.body(LINK_TO_SUBJECT_DETAILS_COLLECTION, equalTo("http://localhost:"+port+"/subject_details"));
	}
	
	@Test
	public void testPostSubjectDetails() {
		Response response = given()
			.contentType(JSON).body(newSubjectDetails)
	   .when()
	   		.post("/subject_details")
	   	.then()
	   		.statusCode(CREATED)
		.extract()
			.response();
		
		String newSubjectDetailsLocation = response.getHeader("location");
		when()
			.get(newSubjectDetailsLocation)
		.then()
			.statusCode(OK)
			.contentType(JSON)
				.body("subjectName", equalTo(newSubjectDetails.getSubjectName()))
				.body("ects", equalTo(newSubjectDetails.getEcts()))
				.body(LINK_TO_SELF, equalTo(newSubjectDetailsLocation)) 						 // http://localhost:8080/subjectDetails/{newSubjectDetails}
				.body(LINK_TO_SUBJECT_DETAILS_COLLECTION, equalTo("http://localhost:"+port+"/subject_details"));  // http://localhost:8080/subjectDetails
	}
	
	@Test
	public void testPostNotUniqueSubjectDetails() {
		given()
			.contentType(JSON)
			.body(newSubjectDetails)
		.when()
			.post("/subject_details")
		.then()
			.statusCode(CREATED);

		given()
			.contentType(JSON)
			.body(newSubjectDetails)
		.when()
			.post("/subject_details")
		.then()
			.statusCode(BAD_REQUEST);
	}
	
	private String getSubjectDetailsRootIfEmbedded(long subjectDetailsId) {
		return String.format(PATH_TO_SUBJECT_DETAILS_COLLECTION,subjectDetailsId);
	}

	private void cleanUpDatabase() {
		jdbcTemplate.execute("DELETE FROM subject_marks");
		jdbcTemplate.execute("DELETE FROM subject");
		jdbcTemplate.execute("DELETE FROM subject_details");
		jdbcTemplate.execute("DELETE FROM student");
	}
	
	private void insertSampleData() {
		SubjectDetails sd = new SubjectDetails("Math",5);
		SubjectDetails sd2 = new SubjectDetails("English",6);
		subjectDetailsRepository.saveAll(Arrays.asList(sd,sd2));
	}
}