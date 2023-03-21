CREATE TABLE IF NOT EXISTS weather_data
(
    id          SERIAL PRIMARY KEY,
    temp        DECIMAL NOT NULL,
    wind_kph    DECIMAL NOT NULL,
    pressure_mb DECIMAL NOT NULL,
    humidity    INT NOT NULL,
    condition   VARCHAR(64) NOT NULL,
    location    VARCHAR(128) NOT NULL,
    timestamp   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
