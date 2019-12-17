package com.redhat.cajun.navy.finding.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ResourceTest {

    //Victim's details are available
    @Test
    public void testGetVictimsCase1() {
        given()
                .pathParam("name", "test-victim-1")
                .when().get("/find/victim/byName/{name}")
                .then()
                .statusCode(200)
                .body(is("{\"empty\":false,\"map\":{\"victims\":{\"empty\":false,\"list\":[{\"empty\":false,\"map\":{\"id\":\"test-incident-id\",\"lat\":\"00.00\",\"lon\":\"00.00\",\"medicalNeeded\":true,\"numberOfPeople\":123,\"victimName\":\"test-victim-1\",\"status\":\"REPORTED\"}}]}}}"));
    }

    //Victim's details are not available
    @Test
    public void testGetVictimsCase2() {
        given()
                .pathParam("name", "test-victim-2")
                .when().get("/find/victim/byName/{name}")
                .then()
                .statusCode(200)
                .body(is("{\"empty\":false,\"map\":{\"victims\":{\"empty\":true,\"list\":[]}}}"));
    }

    //Mission is created and shelter name is available
    @Test
    public void testGetShelterCase1() {
        given()
                .pathParam("incidentId", "test-incident-id-1")
                .when().get("/find/shelter/{incidentId}")
                .then()
                .statusCode(200)
                .body(is("{\"empty\":false,\"map\":{\"status\":true,\"shelter\":{\"empty\":false,\"map\":{\"name\":\"Wilmington Marine Center\",\"lat\":\"34.1706\",\"lon\":\"-77.949\"}}}}"));
    }

    //Mission is created but Shelter name not available
    @Test
    public void testGetShelterCase2() {
        given()
                .pathParam("incidentId", "test-incident-id-2")
                .when().get("/find/shelter/{incidentId}")
                .then()
                .statusCode(200)
                .body(is("{\"empty\":false,\"map\":{\"status\":true,\"shelter\":{\"empty\":false,\"map\":{\"name\":\"Shelter Name not available\",\"lat\":\"00\",\"lon\":\"00\"}}}}"));
    }

    //Mission is not created
    @Test
    public void testGetShelterCase3() {
        given()
                .pathParam("incidentId", "test-incident-id-3")
                .when().get("/find/shelter/{incidentId}")
                .then()
                .statusCode(200)
                .body(is("{\"empty\":false,\"map\":{\"status\":false,\"Desc\":\"Mission not found\"}}"));
    }

}
