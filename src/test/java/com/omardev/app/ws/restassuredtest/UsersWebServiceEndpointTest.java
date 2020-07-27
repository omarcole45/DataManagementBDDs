package com.omardev.app.ws.restassuredtest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(Alphanumeric.class)
class UsersWebServiceEndpointTest {
	
	private final String CONTEXT_PATH= "/omar-app-ws";
	private final String EMAIL_ADDRESS="air_man_j@yahoo.com";
	private final String JSON = "application/json";
	private static String authorization;
	private static String userId;
	private static List<Map<String, String>> addresses;

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=8080;
	}
	
	/**
	 * testUserLogin()
	 * 
	 * This tests the user/login resources
	 * 
	 * Tests are running in alphabetic order using @TestMethodOrder(Alphanumeric.class), hence
	 * the method name is 'a()'
	 */

	@Test
	void a() {
		Map<String, Object> loginDetails = new HashMap<>();
		loginDetails.put("email",EMAIL_ADDRESS);
		loginDetails.put("password","test");
		
		Response response = given().contentType(JSON).
		accept(JSON).body(loginDetails).when().post(CONTEXT_PATH + "/users/login").
		then().
		statusCode(200).
		contentType("").//For now i have content type returning an empty string
		extract().
		response();
		
		 authorization = response.header("Authorization");
		userId= response.header("UserId");
		
		assertNotNull(authorization);
		assertNotNull(userId);
		
		
	}
	/**
	 * testGetUser()
	 * 
	 * This tests the getUser resource.
	 * 
	 * Tests are running in alphabetic order using @TestMethodOrder(Alphanumeric.class), hence
	 * the method name is 'b()'
	 */
	@Test
	void b() {
		
		Response response = given()
				.pathParam("id", userId)
				.header("Authorization", authorization)
				.accept(JSON).when()
				.get(CONTEXT_PATH + "/users/{id}").then().statusCode(200).contentType(JSON).extract().response();
		
		String userPublicId = response.jsonPath().getString("userId");
		String userEmail = response.jsonPath().getString("email");
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		addresses = response.jsonPath().getList("addresses");
		String addressId = addresses.get(0).get("addressId").toString();
		
		
		assertNotNull(userPublicId);
		assertNotNull(userEmail);
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals(EMAIL_ADDRESS, userEmail);
		assertTrue(addresses.size() == 2);
		assertTrue(addressId.length() == 30);
		
	}
	
	@Test
	void testUpdateUser() {
		
		Map<String, Object> userDetails = new HashMap<>();
		
		userDetails.put("firstName", "Jack");
		userDetails.put("lastName", "frost");
		//userDetails.put("password", "123");
		
		Response response = given().header("Authorization", authorization).pathParam("id", userId)
				.contentType(JSON)
				.accept(JSON)
		        .body(userDetails).when().put(CONTEXT_PATH + "/users/{id}").then().statusCode(200)
		        .contentType(JSON)
		        .extract().response();
		
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		List<Map<String, String>> storedAddresses = response.jsonPath().getList("addresses");
		String addressId = storedAddresses.get(0).get("addressId");
		
		
		assertEquals(userDetails.get("firstName"), firstName);
		assertEquals(userDetails.get("lastName"), lastName);
		assertNotNull(storedAddresses);
		assertTrue(addresses.size() == storedAddresses.size());
		assertEquals(addresses.get(0).get("streetName"), storedAddresses.get(0).get("streetName"));
		
	}

}
