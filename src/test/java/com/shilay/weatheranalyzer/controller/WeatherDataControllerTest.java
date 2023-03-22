package com.shilay.weatheranalyzer.controller;

import com.shilay.weatheranalyzer.dto.DateRequest;
import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.dto.WeatherDataDto;
import com.shilay.weatheranalyzer.service.WeatherDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherDataController.class)
class WeatherDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherDataService weatherDataService;

    @Test
    void getLastDataIsOk() throws Exception {
        WeatherDataDto weatherDataDto = WeatherDataDto.builder()
                .temp(6.0)
                .windKph(6.8)
                .pressureMb(1019.0)
                .humidity(65)
                .condition("Overcast")
                .location("Minsk")
                .timestamp(LocalDateTime.of(2023, 3, 19, 23, 28, 59))
                .build();

        when(weatherDataService.getLastData()).thenReturn(weatherDataDto);

        mockMvc.perform(get("/weather-data/last"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Last weather data retrieved"))
                .andExpect(jsonPath("$.data.last_data.temp").value("6.0"))
                .andExpect(jsonPath("$.data.last_data.wind_kph").value("6.8"))
                .andExpect(jsonPath("$.data.last_data.pressure_mb").value("1019.0"))
                .andExpect(jsonPath("$.data.last_data.humidity").value("65"))
                .andExpect(jsonPath("$.data.last_data.condition").value("Overcast"))
                .andExpect(jsonPath("$.data.last_data.location").value("Minsk"));
    }

    @Test
    void getAvgDataIsOk() throws Exception {
        DateRequest dateRequest = new DateRequest();
        dateRequest.setFrom(LocalDate.of(2023, 3, 16));
        dateRequest.setTo(LocalDate.of(2023, 3, 18));
        WeatherDataAvg weatherDataAvg = new WeatherDataAvg(4.0, 10.75, 1021.5, 52.5);
        when(weatherDataService.getAvgData(dateRequest)).thenReturn(weatherDataAvg);

        mockMvc.perform(post("/weather-data/average")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"from\": \"16-03-2023\"," +
                                " \"to\": \"18-03-2023\"" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Average weather data retrieved"))
                .andExpect(jsonPath("$.data.avg_data.avg_temp").value("4.0"))
                .andExpect(jsonPath("$.data.avg_data.avg_wind_kph").value("10.75"))
                .andExpect(jsonPath("$.data.avg_data.avg_pressure_mb").value("1021.5"))
                .andExpect(jsonPath("$.data.avg_data.avg_humidity").value("52.5"));

    }
}
