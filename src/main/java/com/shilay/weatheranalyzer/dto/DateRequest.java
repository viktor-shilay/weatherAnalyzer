package com.shilay.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRequest {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate from;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate to;
}
