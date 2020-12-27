package com.exercise.validation;

import com.exercise.model.NoiseLevelMeasurement;
import com.exercise.utils.Constants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class NoiseLevelMeasurementValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NoiseLevelMeasurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NoiseLevelMeasurement noiseLevelMeasurement = (NoiseLevelMeasurement) target;

        if (noiseLevelMeasurement.getSensorId() == null ||
                noiseLevelMeasurement.getSensorId().trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.MISSING_SENSOR_ID_EXCEPTION_MESSAGE);
        }

        if (noiseLevelMeasurement.getTimestamp() == null) {
            throw new IllegalArgumentException(Constants.MISSING_TIMESTAMP_EXCEPTION_MESSAGE);
        }

        if (noiseLevelMeasurement.getValue() == null) {
            throw new IllegalArgumentException(Constants.MISSING_MEASURED_VALUE_EXCEPTION_MESSAGE);
        }
    }
}
