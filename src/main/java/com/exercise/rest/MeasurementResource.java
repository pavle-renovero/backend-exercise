package com.exercise.rest;

import com.exercise.model.NoiseLevelMeasurement;
import com.exercise.model.NoiseLevelMedian;
import com.exercise.service.MeasurementService;
import com.exercise.validation.NoiseLevelMeasurementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/measurements")
public class MeasurementResource {

    @Autowired
    private MeasurementService measurementService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new NoiseLevelMeasurementValidator());
    }

    @GetMapping
    ResponseEntity getNoiseLevelMedians(@RequestParam String sensorId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Collection<NoiseLevelMedian> noiseLevelMedians = measurementService.getNoiseLevelMedians(sensorId, start, end);
        return ResponseEntity.ok(noiseLevelMedians);
    }

    @PostMapping
    ResponseEntity addMeasurement(@RequestBody @Valid NoiseLevelMeasurement noiseLevelMeasurement) {
        measurementService.addNoiseLevelMeasurement(noiseLevelMeasurement);
        return ResponseEntity.ok().build();
    }

}
