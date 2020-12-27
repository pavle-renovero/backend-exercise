package com.exercise.rest;

import com.exercise.BaseIntegrationTest;
import com.exercise.model.NoiseLevelMedian;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class MeasurementResourceIntegrationTest extends BaseIntegrationTest {

    @Test
    public void getMediansRequestWithoutSensorIdParamShouldReturnBadRequest() {
        when().
                get("/measurements?start=2020-12-23T20:45:00&end=2020-12-23T23:45:00").
                then().
                statusCode(BAD_REQUEST.value());
    }

    @Test
    public void getMediansRequestWithoutStartParamShouldReturnBadRequest() {
        when().
                get("/measurements?sensorId=sensor-bertastreet-21&end=2020-12-23T23:45:00").
                then().
                statusCode(BAD_REQUEST.value());
    }

    @Test
    public void getMediansRequestWithoutEndParamShouldReturnBadRequest() {
        when().
                get("/measurements?sensorId=sensor-bertastreet-21&start=2020-12-23T20:45:00").
                then().
                statusCode(BAD_REQUEST.value());
    }

    @Test
    public void getMediansRequestWithWrongStartParamValueShouldReturnBadRequest() {
       when().
                get("/measurements?sensorId=sensor-bertastreet-21&start=asdsd&end=2020-12-23T23:45:00").
                then().
                statusCode(BAD_REQUEST.value());
    }

    @Test
    public void getMediansRequestShouldReturnMedians() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minus(1L, ChronoUnit.HOURS);
        LocalDateTime end = now.plus(1L, ChronoUnit.HOURS);

        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"value\": \"5\", \"timestamp\": \"" + now + "\"}").
                when().
                post("/measurements").
                then().statusCode(OK.value());

        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"value\": \"12\", \"timestamp\": \"" + now + "\"}").
                when().
                post("/measurements").
                then().statusCode(OK.value());

        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"value\": \"3\", \"timestamp\": \"" + now + "\"}").
                when().
                post("/measurements").
                then().statusCode(OK.value());

        Thread.sleep(5000);

        NoiseLevelMedian[] noiseLevelMedians = when()
                .get("/measurements?sensorId=sensor-bertastreet-21&start=" + start + "&end=" + end).
                        as(NoiseLevelMedian[].class);

        Assert.assertEquals(new BigDecimal(5), noiseLevelMedians[0].getValue());
    }

    @Test
    public void postMeasurementRequestWithoutSensorIdShouldReturnBadRequest() {
        given().
                contentType(JSON).
                body("{\"value\": \"50\", \"timestamp\": \"2020-12-23T23:46:00\"}").
                when().
                post("/measurements").
                then().statusCode(BAD_REQUEST.value());
    }

    @Test
    public void postMeasurementRequestWithoutValueShouldReturnBadRequest() {
        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"timestamp\": \"2020-12-23T23:46:00\"}").
                when().
                post("/measurements").
                then().statusCode(BAD_REQUEST.value());
    }

    @Test
    public void postMeasurementRequestWithoutTimestampShouldReturnBadRequest() {
        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"value\": \"50\"}").
                when().
                post("/measurements").
                then().statusCode(BAD_REQUEST.value());
    }

    @Test
    public void postMeasurementRequestShouldAddNewMeasurement() {
        given().
                contentType(JSON).
                body("{\"sensorId\": \"sensor-bertastreet-21\", \"value\": \"5\", \"timestamp\": \"2020-12-23T23:46:00\"}").
                when().
                post("/measurements").
                then().
                statusCode(OK.value());
    }
}
