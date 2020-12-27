package com.exercise.service.impl;

import com.exercise.generated.renovero.tables.Measurement;
import com.exercise.generated.renovero.tables.records.MeasurementRecord;
import com.exercise.generated.renovero.tables.records.MedianRecord;
import com.exercise.model.NoiseLevelMeasurement;
import com.exercise.model.NoiseLevelMedian;
import com.exercise.service.MeasurementService;
import com.exercise.utils.Constants;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.exercise.generated.renovero.tables.Median.MEDIAN;
import static com.exercise.generated.renovero.tables.Sensor.SENSOR;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    @Autowired
    private DSLContext create;

    @Override
    public Collection<NoiseLevelMedian> getNoiseLevelMedians(String sensorId, LocalDateTime start, LocalDateTime end) {

        if (sensorId == null || sensorId.trim().isEmpty()) {
            throw new IllegalArgumentException(Constants.MISSING_SENSOR_ID_EXCEPTION_MESSAGE);
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException(Constants.MISSING_START_OR_END_TIMESTAMP_EXCEPTION_MESSAGE);
        }

        return create.
                selectFrom(MEDIAN).where(MEDIAN.SENSOR_ID.eq(create.fetchSingle(SENSOR, SENSOR.SENSOR_PUBLIC_ID.
                eq(sensorId)).getId())).
                and(MEDIAN.TIMESTAMP.
                between(Timestamp.valueOf(start), Timestamp.valueOf(end))).
                fetch().
                stream().
                map(medianToNoiseLevelMedian).
                collect(Collectors.toCollection(ArrayList::new));
    }

    private Function<MedianRecord, NoiseLevelMedian> medianToNoiseLevelMedian
            = medianRecord -> new NoiseLevelMedian(medianRecord.getValue(), medianRecord.getTimestamp().toLocalDateTime());

    @Override
    @Transactional
    public void addNoiseLevelMeasurement(NoiseLevelMeasurement noiseLevelMeasurement) {
        MeasurementRecord measurementRecord = create.newRecord(Measurement.MEASUREMENT);
        measurementRecord.setSensorId(create.fetchSingle(SENSOR, SENSOR.SENSOR_PUBLIC_ID.
                eq(noiseLevelMeasurement.getSensorId())).getId());
        measurementRecord.setTimestamp(Timestamp.valueOf(noiseLevelMeasurement.getTimestamp()));
        measurementRecord.setValue(noiseLevelMeasurement.getValue());
            measurementRecord.store();
    }
}
