package com.integration.test;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.person.Person;

public class PersonControllerTest extends AbstractControllerTest {

	private String collectionPrefix = "_embedded.personResources[%1$s].";
	private String links = "_links.";
	private String linkToSelf = links+"self.href";
	private String linkToPersonsCollection = links+"persons.href";
	private long firstPersonId;
	private long secondPersonId;
	
	@Before
	public void setUp() {
		super.setUp();
		List<Person> list = personRepository.findAll();
		firstPersonId = list.get(0).getId();
		secondPersonId = list.get(1).getId();
	}

	@Test
	public void getAllPersons() {
		when().get("/persons")
		.then().statusCode(200)
		.and().contentType(ContentType.JSON)
		.and().body(getCollectionElem("firstName",0), equalTo("Jan"))
		.and().body(getCollectionElem("lastName",0), equalTo("Nowak"))
		.and().body(getCollectionElem("subjectResource.subjectName",0), hasItems("Math","English"))
		.and().body(getCollectionElem(linkToSelf,0), equalTo(getSingleLink(firstPersonId)))
		.and().body(getCollectionElem(linkToPersonsCollection,0), equalTo(getCollectionLink()))
		
		.and().body(getCollectionElem("firstName",1), equalTo("Marcin"))
		.and().body(getCollectionElem("lastName",1), equalTo("Kowalski"))
		.and().body(getCollectionElem("subjectResource.subjectName",1), hasItems("English"))
		.and().body(getCollectionElem(linkToSelf,1), equalTo(getSingleLink(secondPersonId)))
		.and().body(getCollectionElem(linkToPersonsCollection,1), equalTo(getCollectionLink()));
	}
	
	@Test
	public void getPerson() {
		when().get("/persons/{id}",firstPersonId)
		.then().statusCode(200)
		.and().contentType(ContentType.JSON)
		.and().body("firstName", equalTo("Jan"))
		.and().body("lastName", equalTo("Nowak"))
		.and().body("subjectResource.subjectName", hasItems("Math","English"))
		.and().body(linkToSelf, equalTo(getSingleLink(firstPersonId)))
		.and().body(linkToPersonsCollection, equalTo(getCollectionLink()));
	}

	private String getCollectionElem(String paramName, int pathToResource) {
		return String.format(collectionPrefix+paramName, pathToResource);
	}

	private String getSingleLink(long personId) {
		return getCollectionLink() + "/" + personId;
	}
	
	private String getCollectionLink() {
		return getHost() + "/persons";
	}

}
