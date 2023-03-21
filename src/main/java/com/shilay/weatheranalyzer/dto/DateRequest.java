package com.shilay.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DateRequest {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate from;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate to;
}
