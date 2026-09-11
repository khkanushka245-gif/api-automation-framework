package com.example.services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Service object for the Users / Posts resources — the API equivalent of a Page
 * Object. It encapsulates endpoints and request building; it performs NO
 * assertions. Tests decide what to assert on the returned {@link Response}.
 *
 * Keeping assertions out of the service keeps it reusable and keeps each test's
 * intent explicit (a key to stable, readable tests).
 */
public class UserService {

    private final RequestSpecification spec;

    public UserService(RequestSpecification spec) {
        this.spec = spec;
    }

    /** GET /users/{id} */
    public Response getUser(int id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then().extract().response();
    }

    /** GET /users */
    public Response listUsers() {
        return given().spec(spec)
                .when().get("/users")
                .then().extract().response();
    }

    /** POST /posts with a JSON body. */
    public Response createPost(String title, String body, int userId) {
        return given().spec(spec)
                .body(Map.of("title", title, "body", body, "userId", userId))
                .when().post("/posts")
                .then().extract().response();
    }
}
