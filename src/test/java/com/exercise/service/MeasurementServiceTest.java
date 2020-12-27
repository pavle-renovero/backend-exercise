package com.exercise.service;

import com.exercise.JOOQMockProvider;

import com.exercise.generated.renovero.tables.records.MeasurementRecord;
import com.exercise.model.NoiseLevelMeasurement;
import com.exercise.model.NoiseLevelMedian;
import com.exercise.service.impl.MeasurementServiceImpl;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MeasurementRecord.class})
public class MeasurementServiceTest {

    @InjectMocks
    MeasurementServiceImpl measurementService;

    @Spy
    DSLContext create = DSL.using(new MockConnection(new JOOQMockProvider()), SQLDialect.H2);;

    @Test(expected = IllegalArgumentException.class)
    public void getNoiseLevelMediansWithoutSensorIdParamShouldThrowException() {
      measurementService.getNoiseLevelMedians(null, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNoiseLevelMediansWithoutStartDateParamShouldThrowException() {
        measurementService.getNoiseLevelMedians("sensorId", null, LocalDateTime.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNoiseLevelMediansWithoutEndDateParamShouldThrowException() {
        measurementService.getNoiseLevelMedians("sensorId", LocalDateTime.now(), null);
    }

    @Test
    public void getNoiseLevelMedianShouldReturnMedian() {
        List<NoiseLevelMedian> records = (List<NoiseLevelMedian>) measurementService
                .getNoiseLevelMedians("sensorId", LocalDateTime.now(), LocalDateTime.now());

        Assert.assertEquals(new BigDecimal(50), records.get(0).getValue());
        Assert.assertEquals(LocalDateTime.of(2020, 12, 23, 15, 30),
                records.get(0).getStartingTime());
    }

    @Test
    public void addNoiseLevelMeasurementShouldStoreRecord() {
        MeasurementRecord measurementRecord = PowerMockito.spy(new MeasurementRecord());
        Mockito.doReturn(measurementRecord).when(create).newRecord(ArgumentMatchers.any(Table.class));
        PowerMockito.doReturn(1).when(measurementRecord).store();

        measurementService.addNoiseLevelMeasurement(new NoiseLevelMeasurement("sensorId",
                new BigDecimal(40), LocalDateTime.now()));

        Mockito.verify(measurementRecord,times(1)).store();
    }
}