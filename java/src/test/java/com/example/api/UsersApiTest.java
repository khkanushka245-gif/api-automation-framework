package com.example.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UsersApiTest {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    @Test
    public void getUserReturnsExpectedFields() {
        given()
            .baseUri(BASE_URI)
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("email", notNullValue());
    }

    @Test
    public void createPostReturns201() {
        given()
            .baseUri(BASE_URI)
            .header("Content-Type", "application/json")
            .body("{\"title\":\"demo\",\"body\":\"sample body\",\"userId\":1}")
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("title", equalTo("demo"));
    }
}
