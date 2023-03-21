package com.shilay.weatheranalyzer.util;

import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.entity.WeatherData;

public class WeatherConverter {

    public static WeatherDataDto convertToDto(WeatherData weatherData) {
        return WeatherDataDto.builder()
                .temp(weatherData.getTemp())
                .windKph(weatherData.getWindKph())
                .pressureMb(weatherData.getPressureMb())
                .humidity(weatherData.getHumidity())
                .condition(weatherData.getCondition())
                .location(weatherData.getLocation())
                .timestamp(weatherData.getTimestamp())
                .build();
    }

    public static WeatherData convertToEntity(WeatherDataDto weatherDataDto) {
        return WeatherData.builder()
                .temp(weatherDataDto.getTemp())
                .windKph(weatherDataDto.getWindKph())
                .pressureMb(weatherDataDto.getPressureMb())
                .humidity(weatherDataDto.getHumidity())
                .condition(weatherDataDto.getCondition())
                .location(weatherDataDto.getLocation())
                .timestamp(weatherDataDto.getTimestamp())
                .build();
    }
}
