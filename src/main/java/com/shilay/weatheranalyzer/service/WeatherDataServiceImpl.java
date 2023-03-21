package com.shilay.weatheranalyzer.service;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.entity.WeatherData;
import com.shilay.weatheranalyzer.exception.DateRequestException;
import com.shilay.weatheranalyzer.exception.WeatherDataNotFoundException;
import com.shilay.weatheranalyzer.repository.WeatherDataRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    @Value("${X-RapidAPI-Key}")
    private String xRapidAPIKey;

    @Value("${X-RapidAPI-Host}")
    private String xRapidAPIHost;

    @Value("${external-api-url}")
    private String externalAPIUrl;

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;

    public WeatherDataServiceImpl(WeatherDataRepository weatherDataRepository, RestTemplate restTemplate) {
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = restTemplate;
    }

    public WeatherDataDto getLastData() {
        log.info("Fetching last weather data");
        Optional<WeatherData> lastData =
                weatherDataRepository.findTopByOrderByIdDesc();
        WeatherData weatherData = lastData.orElseThrow(() -> new WeatherDataNotFoundException("No data found from database"));
        return convertToDto(weatherData);
    }

    public WeatherDataAvg getAvgData(DateRequest request) {
        if (request.getFrom() == null | request.getTo() == null) {
            throw new DateRequestException("Dates shouldn't be empty!");
        }
        if (request.getFrom().isAfter(request.getTo())) {
            throw new DateRequestException("'From' date is greater than 'to' date");
        }
        log.info("Fetching average weather data from {} to {}", request.getFrom(), request.getTo());

        WeatherDataAvg avg = weatherDataRepository.getAvgData(request.getFrom().atStartOfDay(), request.getTo().atStartOfDay());
        if (avg.getAvgTemp() == null | avg.getAvgWindKph() == null | avg.getAvgPressureMb() == null | avg.getAvgHumidity() == null) {
            throw new DateRequestException("No average weather data found from this data range");
        }
        return avg;
    }

    @Scheduled(fixedRateString = "${save-frequency-minutes}", timeUnit = TimeUnit.MINUTES)
    public void save() {
        log.info("Saving weather data into database");
        weatherDataRepository.save(getDataFromExternalAPI());
    }

    private WeatherData getDataFromExternalAPI() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", xRapidAPIKey);
        headers.set("X-RapidAPI-Host", xRapidAPIHost);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<WeatherDataDto> response = restTemplate.exchange(
                externalAPIUrl, HttpMethod.GET, requestEntity, WeatherDataDto.class);
        Optional<WeatherDataDto> body = Optional.ofNullable(response.getBody());
        return convertToEntity(body.orElseThrow(() -> new WeatherDataNotFoundException("No data found from external API")));
    }

    private WeatherDataDto convertToDto(WeatherData weatherData) {
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

    private WeatherData convertToEntity(WeatherDataDto weatherDataDto) {
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
