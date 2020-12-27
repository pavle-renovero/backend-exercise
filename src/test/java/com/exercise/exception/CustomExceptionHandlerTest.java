package com.exercise.exception;

import com.exercise.utils.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RunWith(MockitoJUnitRunner.class)
public class CustomExceptionHandlerTest {

    @InjectMocks
    CustomExceptionHandler customExceptionHandler;

    @Test
    public void testHandleHttpMessageNotReadable() {
        HttpMessageNotReadableException ex = Mockito.mock(HttpMessageNotReadableException.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = Mockito.mock(WebRequest.class);

        ResponseEntity entity = customExceptionHandler
                .handleHttpMessageNotReadable(ex, headers, status, request);

        Assert.assertEquals(Constants.INVALID_DATA_FORMAT_MESSAGE,
                ((ErrorResponse) entity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                ((ErrorResponse) entity.getBody()).getResponseStatus());
    }

    @Test
    public void testHandleMissingServletRequestParameter() {
        MissingServletRequestParameterException ex = Mockito.mock(MissingServletRequestParameterException.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest request = Mockito.mock(WebRequest.class);

        ResponseEntity entity = customExceptionHandler
                .handleMissingServletRequestParameter(ex, headers, status, request);

        Assert.assertEquals(Constants.MISSING_SERVLET_REQUEST_PARAM_MESSAGE,
                ((ErrorResponse) entity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                ((ErrorResponse) entity.getBody()).getResponseStatus());
    }

    @Test
    public void testHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = Mockito.mock(MethodArgumentTypeMismatchException.class);
        WebRequest request = Mockito.mock(WebRequest.class);

        ResponseEntity entity = customExceptionHandler
                .handleMethodArgumentTypeMismatchException(ex, request);

        Assert.assertEquals(Constants.INVALID_DATA_FORMAT_MESSAGE,
                ((ErrorResponse) entity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                ((ErrorResponse) entity.getBody()).getResponseStatus());
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = Mockito.mock(IllegalArgumentException.class);
        WebRequest request = Mockito.mock(WebRequest.class);

        Mockito.when(ex.getMessage()).thenReturn(Constants.MISSING_SENSOR_ID_EXCEPTION_MESSAGE);

        ResponseEntity entity = customExceptionHandler
                .handleIllegalArgumentException(ex, request);

        Assert.assertEquals(Constants.MISSING_SENSOR_ID_EXCEPTION_MESSAGE,
                ((ErrorResponse) entity.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,
                ((ErrorResponse) entity.getBody()).getResponseStatus());
    }
}
