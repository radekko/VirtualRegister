package com.integration.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.core.Application;
import com.jayway.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

	@LocalServerPort
	int port;
	
	public void setUp() {
		RestAssured.port = port;
	}
	
	protected String getHost() {
		return "http://localhost:"+port;
	}
}
