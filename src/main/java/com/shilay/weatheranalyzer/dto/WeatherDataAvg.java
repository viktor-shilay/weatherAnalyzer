package com.shilay.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataAvg {

    @JsonProperty("avg_temp")
    private Double avgTemp;

    @JsonProperty("avg_wind_kph")
    private Double avgWindKph;

    @JsonProperty("avg_pressure_mb")
    private Double avgPressureMb;

    @JsonProperty("avg_humidity")
    private Double avgHumidity;
}
