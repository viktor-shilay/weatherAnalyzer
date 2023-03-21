package com.shilay.weatheranalyzer.controller;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.response.Response;
import com.shilay.weatheranalyzer.service.WeatherDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("weather-data")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    public WeatherDataController(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @GetMapping("/last")
    public ResponseEntity<Response> getLastData() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Collections.singletonMap("last_data", weatherDataService.getLastData()))
                        .message("Last weather data retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/average")
    public ResponseEntity<Response> getAvgData(@RequestBody DateRequest dateRequest) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Collections.singletonMap("avg_data", weatherDataService.getAvgData(dateRequest)))
                        .message("Average weather data retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
