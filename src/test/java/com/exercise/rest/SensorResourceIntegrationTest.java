package com.exercise.rest;

import com.exercise.BaseIntegrationTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.HttpStatus.OK;

public class SensorResourceIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_preconfigured_sensors() {
        when().
                get("/sensors").
        then().
                statusCode(OK.value()).
                body("sensorId", hasItems("sensor-longstreet-45", "sensor-bertastreet-21", "sensor-werdstreet-2"));
    }

    @Test
    public void should_add_new_sensor() {
        // add sensor
        given().
                contentType(JSON).
                body("{ \"sensorId\": \"sensor-teststreet-1\" }").
        when().
                post("/sensors").

        then().
                statusCode(OK.value());

        // get sensors
        when().
                get("/sensors").
        then().
                statusCode(OK.value()).
                body("sensorId", hasItems("sensor-teststreet-1"));
    }

}
