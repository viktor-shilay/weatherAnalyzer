package com.shilay.weatheranalyzer.service;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.entity.WeatherData;
import com.shilay.weatheranalyzer.exception.DateRequestException;
import com.shilay.weatheranalyzer.exception.WeatherDataNotFoundException;
import com.shilay.weatheranalyzer.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherDataServiceImplTest {

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private WeatherDataServiceImpl weatherDataServiceImpl;

    @Test
    void getLastData() {
        WeatherData weatherData = WeatherData.builder()
                .id(6L)
                .temp(6.0)
                .windKph(6.8)
                .pressureMb(1019.0)
                .humidity(65)
                .condition("Overcast")
                .location("Minsk")
                .timestamp(LocalDateTime.of(2023, 3, 19, 23, 28, 59))
                .build();
        WeatherDataDto weatherDataDto = WeatherDataDto.builder()
                .temp(6.0)
                .windKph(6.8)
                .pressureMb(1019.0)
                .humidity(65)
                .condition("Overcast")
                .location("Minsk")
                .timestamp(LocalDateTime.of(2023, 3, 19, 23, 28, 59))
                .build();

        when(weatherDataRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.of(weatherData));
        WeatherDataDto actual = weatherDataServiceImpl.getLastData();

        assertThat(actual).isEqualTo(weatherDataDto);
    }

    @Test
    void getLastDataThrowExceptionIfNoData() {

        when(weatherDataRepository.findTopByOrderByTimestampDesc()).thenReturn(Optional.empty());

        assertThrows(WeatherDataNotFoundException.class, () -> weatherDataServiceImpl.getLastData());
    }

    @Test
    void getAvgData() {
        DateRequest dateRequest = new DateRequest();
        dateRequest.setFrom(LocalDate.of(2023, 3, 16));
        dateRequest.setTo(LocalDate.of(2023, 3, 18));

        WeatherDataAvg weatherDataAvg = new WeatherDataAvg(4.0, 10.75, 1021.5, 52.5);

        when(weatherDataRepository.getAvgData(dateRequest.getFrom().atStartOfDay(), dateRequest.getTo().atStartOfDay()))
                .thenReturn(weatherDataAvg);
        WeatherDataAvg actual = weatherDataServiceImpl.getAvgData(dateRequest);

        assertThat(actual).isEqualTo(weatherDataAvg);
    }

    @Test
    void getAvgDataThrowExceptionIfWrongDate() {
        assertThrows(DateRequestException.class, () -> weatherDataServiceImpl.getAvgData(new DateRequest(null, null)));
        assertThrows(DateRequestException.class, () -> weatherDataServiceImpl.getAvgData(new DateRequest(LocalDate.of(2023, 3, 18), LocalDate.of(2023, 3, 16))));
        assertThrows(DateRequestException.class, () -> weatherDataServiceImpl.getAvgData(new DateRequest(LocalDate.of(2023, 3, 16), null)));
    }
}
