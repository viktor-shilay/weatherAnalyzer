CREATE TABLE IF NOT EXISTS weather_data
(
    id
    SERIAL
    PRIMARY
    KEY,
    temp
    DECIMAL
    NOT
    NULL,
    wind_kph
    DECIMAL
    NOT
    NULL,
    pressure_mb
    DECIMAL
    NOT
    NULL,
    humidity
    INT
    NOT
    NULL,
    condition
    VARCHAR
(
    64
) NOT NULL,
    location VARCHAR
(
    128
) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO weather_data (temp, wind_kph, pressure_mb, humidity, condition, location, timestamp)
VALUES (3.0, 9.4, 1022.0, 52, 'Clear', 'Minsk', '2023-03-16 12:56:01'),
       (5, 12.1, 1021, 53, 'Clear', 'Minsk', '2023-03-17 13:41:01'),
       (7, 14.1, 1021, 54, 'Sunny', 'Minsk', '2023-03-18 12:22:04'),
       (10, 19.1, 1020, 53, 'Sunny', 'Minsk', '2023-03-19 14:33:11'),
       (7, 18.5, 1019, 55, 'Cloudy', 'Minsk', '2023-03-19 18:13:37'),
       (6, 6.8, 1019, 65, 'Overcast', 'Minsk', '2023-03-19 23:28:59');
