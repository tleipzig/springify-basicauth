package com.it_surmann.springify.basicauth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Main test class for springify-basicauth. A simple Spring Boot web application is started and called via
 * HTTP to check the results.
 */
@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("enabled")
public class BasicAuthEnabledIT {

    @LocalServerPort
    private int serverPort;

    @Test
    public void basicAuthEnabled() {
        log.info("Running basicAuthEnabled()");

        ExtractableResponse<Response> response = RestAssured.given()
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());

        response = RestAssured.given()
                .auth().preemptive().basic("basicuser", "wrongpass")
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());

        response = RestAssured.given()
                .auth().preemptive().basic("basicuser", "basicpass")
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

}
