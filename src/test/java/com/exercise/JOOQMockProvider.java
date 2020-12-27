package com.exercise;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.exercise.generated.renovero.tables.Median.MEDIAN;
import static com.exercise.generated.renovero.tables.Measurement.MEASUREMENT;
import static com.exercise.generated.renovero.tables.Sensor.SENSOR;

public class JOOQMockProvider implements MockDataProvider {

    @Override
    public MockResult[] execute(MockExecuteContext ctx) {
        DSLContext create = DSL.using(SQLDialect.H2);
        MockResult[] mock = new MockResult[1];
        String sql = ctx.sql();

        switch(sql.toUpperCase()) {
            case "SELECT \"RENOVERO\".\"SENSOR\".\"ID\", " +
                    "\"RENOVERO\".\"MEASUREMENT\".\"VALUE\" FROM \"RENOVERO\".\"SENSOR\"" +
                    " JOIN \"RENOVERO\".\"MEASUREMENT\" ON \"RENOVERO\".\"MEASUREMENT\".\"SENSOR_ID\"" +
                    " = \"RENOVERO\".\"SENSOR\".\"ID\" WHERE \"RENOVERO\".\"MEASUREMENT\".\"TIMESTAMP\"" +
                    " BETWEEN CAST(? AS TIMESTAMP) AND CAST(? AS TIMESTAMP)":

                Result<Record2<Long, BigDecimal>> measurementResult =
                        create.newResult(SENSOR.ID, MEASUREMENT.VALUE);
                measurementResult.add(create
                        .newRecord(SENSOR.ID,MEASUREMENT.VALUE)
                        .values(1L, new BigDecimal(50)));
                mock[0] = new MockResult(1, measurementResult);
                break;

            case "SELECT \"RENOVERO\".\"SENSOR\".\"ID\", " +
                    "\"RENOVERO\".\"SENSOR\".\"SENSOR_PUBLIC_ID\" FROM \"RENOVERO\".\"SENSOR\" WHERE " +
                    "\"RENOVERO\".\"SENSOR\".\"SENSOR_PUBLIC_ID\" = CAST(? AS VARCHAR)":

                Result<Record2<Long, String>> sensorResult =
                        create.newResult(SENSOR.ID, SENSOR.SENSOR_PUBLIC_ID);
                sensorResult.add(create
                        .newRecord(SENSOR.ID, SENSOR.SENSOR_PUBLIC_ID)
                        .values(1L, "sensor-longstreet-45"));
                mock[0] = new MockResult(1, sensorResult);
                break;

            case "SELECT \"RENOVERO\".\"MEDIAN\".\"ID\"," +
                    " \"RENOVERO\".\"MEDIAN\".\"SENSOR_ID\", \"RENOVERO\".\"MEDIAN\".\"VALUE\"," +
                    " \"RENOVERO\".\"MEDIAN\".\"TIMESTAMP\" FROM \"RENOVERO\".\"MEDIAN\" WHERE" +
                    " (\"RENOVERO\".\"MEDIAN\".\"SENSOR_ID\" = CAST(? AS BIGINT) AND \"RENOVERO\".\"MEDIAN\".\"TIMESTAMP\"" +
                    " BETWEEN CAST(? AS TIMESTAMP) AND CAST(? AS TIMESTAMP))":

                Result<Record4<Long, Long, BigDecimal, Timestamp>> medianResult =
                        create.newResult(MEDIAN.ID, MEDIAN.SENSOR_ID,MEDIAN.VALUE, MEDIAN.TIMESTAMP);
                medianResult.add(create
                        .newRecord(MEDIAN.ID, MEDIAN.SENSOR_ID,MEDIAN.VALUE, MEDIAN.TIMESTAMP)
                        .values(1L, 2L, new BigDecimal(50),
                                Timestamp.valueOf(LocalDateTime.of(2020, 12, 23, 15, 30))));
                mock[0] = new MockResult(1, medianResult);
                break;
        }
        return mock;
    }
}
