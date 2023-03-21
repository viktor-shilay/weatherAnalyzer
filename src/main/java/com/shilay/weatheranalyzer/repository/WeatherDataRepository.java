package com.shilay.weatheranalyzer.repository;

import com.shilay.weatheranalyzer.dto.WeatherDataAvg;
import com.shilay.weatheranalyzer.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    Optional<WeatherData> findTopByOrderByIdDesc();

    @Query("""
            SELECT new com.shilay.weatheranalyzer.dto.WeatherDataAvg(
            AVG(w.temp) as avgTemp,
            AVG(w.windKph) as avgWindKph, 
            AVG(w.pressureMb) as avgPressureMb,
            AVG(w.humidity) as avgHumidity) 
            FROM WeatherData w 
            WHERE w.timestamp BETWEEN :from AND :to
            """)
    WeatherDataAvg getAvgData(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
