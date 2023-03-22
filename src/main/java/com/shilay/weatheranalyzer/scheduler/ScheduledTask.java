package com.shilay.weatheranalyzer.scheduler;

import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.entity.WeatherData;
import com.shilay.weatheranalyzer.repository.WeatherDataRepository;
import com.shilay.weatheranalyzer.util.WeatherConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class ScheduledTask {

    @Value("${X-RapidAPI-Key}")
    private String xRapidAPIKey;

    @Value("${X-RapidAPI-Host}")
    private String xRapidAPIHost;

    @Value("${external-api-url}")
    private String externalAPIUrl;

    private final WeatherDataRepository weatherDataRepository;
    private final RestTemplate restTemplate;

    public ScheduledTask(WeatherDataRepository weatherDataRepository, RestTemplate restTemplate) {
        this.weatherDataRepository = weatherDataRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRateString = "${save-frequency-minutes}", timeUnit = TimeUnit.MINUTES)
    public void save() {
        log.info("Saving weather data into database");
        Optional<WeatherData> weatherData = getDataFromExternalAPI();
        weatherData.ifPresentOrElse(weatherDataRepository::save, () -> log.info("Data hasn't been saved!"));
    }

    private Optional<WeatherData> getDataFromExternalAPI() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", xRapidAPIKey);
        headers.set("X-RapidAPI-Host", xRapidAPIHost);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Optional<WeatherDataDto> body = Optional.empty();
        try {
            ResponseEntity<WeatherDataDto> response = restTemplate.exchange(
                    externalAPIUrl, HttpMethod.GET, requestEntity, WeatherDataDto.class);
            body = Optional.ofNullable(response.getBody());
        } catch (ResourceAccessException ex) {
            log.warn("External API is not available!");
        }
        if (body.isEmpty()) {
            log.warn("External API did not send required data");
            return Optional.empty();
        }
        return Optional.ofNullable(WeatherConverter.convertToEntity(body.get()));
    }
}
