package com.exercise.service;

import com.exercise.model.NoiseLevelMeasurement;
import com.exercise.model.NoiseLevelMedian;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MeasurementService {

    Collection<NoiseLevelMedian> getNoiseLevelMedians(String sensorId, LocalDateTime start, LocalDateTime end);
    void addNoiseLevelMeasurement(NoiseLevelMeasurement noiseLevelMeasurement);
}
