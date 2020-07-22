package com.omardev.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

class TestCreateUser {
	
	private final String CONTEXT_PATH= "/omar-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=8080;
		
	}

	@Test
	final void testCreateUser() {
		
		List<Map<String, Object>> userAddresses = new ArrayList<>();
		
		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("city", "Vancouver");
		shippingAddress.put("country", "Canada");
		shippingAddress.put("streetName", "123 Street name");
		shippingAddress.put("postalCode", "ABCCBA");
		shippingAddress.put("type", "billing");
		
		Map<String, Object> billingAddress = new HashMap<>();
		billingAddress.put("city", "Vancouver");
		billingAddress.put("country", "Canada");
		billingAddress.put("streetName", "123 Street name");
		billingAddress.put("postalCode", "ABCCBA");
		billingAddress.put("type", "billing");
		
		userAddresses.add(shippingAddress);
		
		
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Omar");
		userDetails.put("lastName", "Coleman");
		userDetails.put("email", "omirriuscoleman3028@yahoo.com");
		userDetails.put("password", "123");
		userDetails.put("addresses", userAddresses);
		
		Response response = given().contentType("application/json").
		accept("application/json").body(userDetails).when().post(CONTEXT_PATH + "/users").
		then().
		statusCode(200).
		contentType("application/json").
		extract().
		response();
		
		String userId = response.jsonPath().getString("userId");
		assertNotNull(userId);
		assertTrue(userId.length() == 30);
		
		String bodyString = response.getBody().asString();
		try {
			JSONObject responseBodyJson = new JSONObject(bodyString);
			JSONArray addresses = responseBodyJson.getJSONArray("addresses");
			assertNotNull(addresses);
			assertTrue(addresses.length() == 1);
			
			String addressId = addresses.getJSONObject(0).getString("addressId");
			assertNotNull(addressId);
			assertTrue(addressId.length() == 30);
		} catch (JSONException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
