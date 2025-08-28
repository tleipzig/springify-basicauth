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
 * Test class for testing springify.basicauth.enabled=false property.
 */
@Slf4j
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("disabled")
public class BasicAuthDisabledIT {

    @LocalServerPort
    private int serverPort;

    @Test
    public void basicAuthDisabled() {
        log.info("Running basicAuthDisabled()");

        final ExtractableResponse<Response> response = RestAssured.given()
                .request().when().get("http://localhost:" + serverPort + "/test.html")
                .then().extract();
        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals("test result", response.asString());
    }

}
