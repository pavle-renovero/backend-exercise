package com.exercise.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private HttpStatus responseStatus;
    private String message;

    public ErrorResponse(HttpStatus responseStatus, String message) {
        super();
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
