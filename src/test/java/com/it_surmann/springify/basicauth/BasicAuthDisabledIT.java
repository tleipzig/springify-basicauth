package com.it_surmann.springify.basicauth;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


/**
 * Test class for testing springify.basicauth.enabled=false property.
 */
@Slf4j
@RunWith(SpringRunner.class)
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
