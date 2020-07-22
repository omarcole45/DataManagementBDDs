package com.omardev.app.ws.restassuredtest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

class UsersWebServiceEndpointTest {
	
	private final String CONTEXT_PATH= "/omar-app-ws";
	private final String EMAIL_ADDRESS="air_man_j@yahoo.com";
	private final String JSON = "application/json";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=8080;
	}

	@Test
	void testUserLogin() {
		Map<String, Object> loginDetails = new HashMap<>();
		loginDetails.put("email",EMAIL_ADDRESS);
		loginDetails.put("password","test");
		
		Response response = given().contentType(JSON).
		accept("application/json").body(loginDetails).when().post(CONTEXT_PATH + "/users/login").
		then().
		statusCode(200).
		contentType("").//For now i have content type returning an empty string
		extract().
		response();
		
		String authorization = response.header("Authorization");
		String UserId= response.header("UserId");
		
		assertNotNull(authorization);
		assertNotNull(UserId);
		
		
	}

}
