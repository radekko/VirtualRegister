package com.integration.test;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import core.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {
	
	public int OK = HttpStatus.OK.value();
	public int CREATED = HttpStatus.CREATED.value();
	public int NOT_FOUND = HttpStatus.NOT_FOUND.value();
	public int NO_CONTENT = HttpStatus.NO_CONTENT.value();
	public int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
	public int CONFLICT = HttpStatus.CONFLICT.value();
	
	public final String LINKS = "_links.";
	public final String LINK_TO_SELF = LINKS + "self.href";
	public final String CONTENT_LENGHT = "Content-Lenght";
	
	@LocalServerPort
	int port;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public void setUp() {
		RestAssured.port = port;
	}
	
	protected String getHost() {
		return "http://localhost:"+port;
	}
	
	protected void isResponseBodyEmpty(Response res) {
		assertThat(res.getHeader(CONTENT_LENGHT), nullValue());
	}
}