package com.example.base;

import com.example.config.EnvConfig;
import com.example.config.RestAssuredConfigFactory;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;

/**
 * Shared TestNG fixture for all API tests.
 *
 * Responsibilities:
 *  - configure the base URI and finite HTTP timeouts once per suite,
 *  - expose immutable, reusable request/response specs.
 *
 * Anti-flakiness notes:
 *  - Specs are built once and never mutated, so tests share no mutable state
 *    and cannot interfere with each other (safe to run in any order / parallel).
 *  - The request spec carries the timeout config, so every call fails fast on a
 *    slow endpoint instead of hanging.
 */
public abstract class BaseApiTest {

    /** Reusable request spec: base URI, JSON, timeouts, optional auth. */
    protected static RequestSpecification requestSpec;

    /** Reusable expectation for a well-formed JSON response. */
    protected static ResponseSpecification jsonResponseSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        RestAssured.config = RestAssuredConfigFactory.timeoutConfig();

        RequestSpecBuilder req = new RequestSpecBuilder()
                .setBaseUri(EnvConfig.baseUri())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        String token = EnvConfig.authToken();
        if (!token.isBlank()) {
            req.addHeader("Authorization", "Bearer " + token);
        }
        requestSpec = req.build();

        jsonResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
