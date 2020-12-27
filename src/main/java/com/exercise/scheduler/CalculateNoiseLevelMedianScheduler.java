package com.exercise.scheduler;

import com.exercise.generated.renovero.tables.records.MedianRecord;
import com.exercise.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.exercise.generated.renovero.tables.Sensor.SENSOR;
import static com.exercise.generated.renovero.tables.Measurement.MEASUREMENT;
import static com.exercise.generated.renovero.tables.Median.MEDIAN;

@Slf4j
@Component
public class CalculateNoiseLevelMedianScheduler {

    @Autowired
    private DSLContext create;

    @Scheduled(fixedRateString = "${median.scheduler.rate}",
            initialDelayString = "${median.scheduler.delay}")
    public void execute() {
        long currentSystemTime = System.currentTimeMillis();
        Timestamp currentTime = new Timestamp(currentSystemTime);
        Timestamp oneHourAgo = new Timestamp(currentSystemTime - Constants.ONE_HOUR);
        log.info("Calculating medians for measurements performed between: {} and {}", oneHourAgo, currentTime);

        create.
                select(SENSOR.ID, MEASUREMENT.VALUE).from(SENSOR).innerJoin(MEASUREMENT).
                on(MEASUREMENT.SENSOR_ID.eq(SENSOR.ID)).
                where(MEASUREMENT.TIMESTAMP.
                between(oneHourAgo).
                and(currentTime)).
                fetch().
                sortAsc(MEASUREMENT.VALUE).
                stream().collect(Collectors.groupingBy(e -> e.getValue(SENSOR.ID), Collectors.toList())).
                forEach((k, v) -> {
            MedianRecord medianRecord = create.newRecord(MEDIAN);
            medianRecord.setValue(this.calculateMedian(v));
            medianRecord.setTimestamp(oneHourAgo);
            medianRecord.setSensorId(k);
            medianRecord.store();
        });
    }

    private BigDecimal calculateMedian(List<Record2<Long, BigDecimal>> sensorMeasurements) {
        if (sensorMeasurements.size() % 2 == 0) {
            BigDecimal firstNum = sensorMeasurements.get(sensorMeasurements.size()/2).getValue(MEASUREMENT.VALUE);
            BigDecimal secondNum = sensorMeasurements.get((sensorMeasurements.size()/2)-1).getValue(MEASUREMENT.VALUE);
            return (firstNum.add(secondNum)).divide(BigDecimal.valueOf(2.0));
        } else {
            return sensorMeasurements.get(sensorMeasurements.size()/2).getValue(MEASUREMENT.VALUE);
        }
    }
}
