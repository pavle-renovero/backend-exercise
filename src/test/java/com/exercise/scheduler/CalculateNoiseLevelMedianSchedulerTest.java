package com.exercise.scheduler;

import com.exercise.JOOQMockProvider;
import com.exercise.generated.renovero.tables.records.MedianRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MedianRecord.class})
public class CalculateNoiseLevelMedianSchedulerTest {

    @InjectMocks
    CalculateNoiseLevelMedianScheduler scheduler;

    @Spy
    DSLContext create = DSL.using(new MockConnection(new JOOQMockProvider()), SQLDialect.H2);;

    @Test
    public void executeShouldCalculateMedianAndStoreToDb() {
        MedianRecord medianRecord = PowerMockito.spy(new MedianRecord());
        Mockito.doReturn(medianRecord).when(create).newRecord(ArgumentMatchers.any(Table.class));
        PowerMockito.doReturn(1).when(medianRecord).store();

        scheduler.execute();

        Mockito.verify(medianRecord,times(1)).store();
    }
}
