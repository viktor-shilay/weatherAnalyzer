package com.shilay.weatheranalyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataDto {

    @JsonProperty("temp")
    private Double temp;

    @JsonProperty("wind_kph")
    private Double windKph;

    @JsonProperty("pressure_mb")
    private Double pressureMb;

    @JsonProperty("humidity")
    private Integer humidity;

    @JsonProperty("condition")
    private String condition;

    private String location;

    @JsonIgnore
    private LocalDateTime timestamp;

    @JsonProperty("location")
    private void unpackNestedLocation(Map<String, Object> location) {
        this.location = (String) location.get("name");
        this.timestamp = LocalDateTime.parse((String) location.get("localtime"), DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"));
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("current")
    private void unpackNestedCurrent(Map<String, Object> current) {
        this.temp = (Double) current.get("temp_c");
        this.windKph = (Double) current.get("wind_kph");
        this.pressureMb = (Double) current.get("pressure_mb");
        this.humidity = (Integer) current.get("humidity");
        Map<String, Object> condition = (Map<String, Object>) current.get("condition");
        this.condition = (String) condition.get("text");
    }
}
