package com.it_surmann.springify.basicauth;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Main test class for springify-basicauth. A simple Spring Boot web application is started and called via
 * HTTP to check the results.
 */
@Slf4j
@RunWith(SpringRunner.class)
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
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());

        response = RestAssured.given()
                .authentication().preemptive().basic("basicuser", "wrongpass")
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode());

        response = RestAssured.given()
                .authentication().preemptive().basic("basicuser", "basicpass")
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        Assert.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

}
