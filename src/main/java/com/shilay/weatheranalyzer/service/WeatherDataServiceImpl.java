package com.shilay.weatheranalyzer.service;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.entity.WeatherData;
import com.shilay.weatheranalyzer.exception.DateRequestException;
import com.shilay.weatheranalyzer.exception.WeatherDataNotFoundException;
import com.shilay.weatheranalyzer.repository.WeatherDataRepository;
import com.shilay.weatheranalyzer.util.WeatherConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;

    public WeatherDataServiceImpl(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    public WeatherDataDto getLastData() {
        log.info("Fetching last weather data");
        Optional<WeatherData> lastData =
                weatherDataRepository.findTopByOrderByTimestampDesc();
        WeatherData weatherData = lastData.orElseThrow(() -> new WeatherDataNotFoundException("No data found from database"));
        return WeatherConverter.convertToDto(weatherData);
    }

    public WeatherDataAvg getAvgData(DateRequest request) {
        if (request.getFrom() == null || request.getTo() == null) {
            throw new DateRequestException("Dates shouldn't be empty!");
        }
        if (request.getFrom().isAfter(request.getTo())) {
            throw new DateRequestException("'From' date is greater than 'to' date");
        }
        log.info("Fetching average weather data from {} to {}", request.getFrom(), request.getTo());

        WeatherDataAvg avg = weatherDataRepository.getAvgData(request.getFrom().atStartOfDay(), request.getTo().atStartOfDay());
        if (avg.getAvgTemp() == null || avg.getAvgWindKph() == null || avg.getAvgPressureMb() == null || avg.getAvgHumidity() == null) {
            throw new DateRequestException("No average weather data found from this data range");
        }
        return avg;
    }

}
