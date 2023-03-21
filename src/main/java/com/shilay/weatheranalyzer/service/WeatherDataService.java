package com.shilay.weatheranalyzer.service;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.dto.WeatherDataDto;

public interface WeatherDataService {

    WeatherDataDto getLastData();

    WeatherDataAvg getAvgData(DateRequest dateRequest);
}
