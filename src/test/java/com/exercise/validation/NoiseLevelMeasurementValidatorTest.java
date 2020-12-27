package com.exercise.validation;

import com.exercise.model.NoiseLevelMeasurement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class NoiseLevelMeasurementValidatorTest {

    @InjectMocks
    NoiseLevelMeasurementValidator noiseLevelMeasurementValidator;

    @Test(expected = IllegalArgumentException.class)
    public void validateWithoutSensorIdParamShouldThrowException() {
        noiseLevelMeasurementValidator.validate(new NoiseLevelMeasurement(null,
                new BigDecimal(40), LocalDateTime.now()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateWithoutValueParamShouldThrowException() {
        noiseLevelMeasurementValidator.validate(new NoiseLevelMeasurement("sensorId",
                null, LocalDateTime.now()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateWithoutTimestampParamShouldThrowException() {
        noiseLevelMeasurementValidator.validate(new NoiseLevelMeasurement("sensorId",
                new BigDecimal(40), null), null);
    }
}
