# Weather Analyzer
## Description
The application receives information about the weather in Minsk from external API ([Weather-API](https://rapidapi.com/weatherapi/api/weatherapi-com)),
according to a schedule, and saves it into the database.

## Features
1. Get the actual weather information
2. Get the average weather information for the specified period
3. Application saves the weather information into database.
   (You can change it in the application.yml file, "save-frequency-minutes" property, default - 2 minutes)

## How to run
The easiest way to run the application is using Docker.
Make sure that you have [Docker](https://www.docker.com/) installed on your machine.
```
# Clone this repository
$ git clone https://github.com/viktor-shilay/weatherAnalyzer.git
$ cd weatherAnalyzer

# Start up application by running docker compose up.
$ docker-compose up -d
```

Now you have weather-app running on port 8080 and postgres database running on 5432 port.

## API Endpoints
To send HTTP requests you can use Swagger (available at http://localhost:8080/api/v1/swagger-ui.html) 
or [Postman](https://www.postman.com/downloads/) or something like that.

### 1. Get the actual weather information

#### 1.1 Request

`GET /api/v1/weather-data/last/`

#### 1.1 Response

    "timeStamp": "2023-03-22T13:17:23.14024",
    "statusCode": 200,
    "status": "OK",
    "message": "Last weather data retrieved",
    "data": {
        "last_data": {
            "location": "Minsk",
            "temp": 6.0,
            "wind_kph": 11.2,
            "pressure_mb": 1015.0,
            "humidity": 75,
            "condition": "Partly cloudy"
        }
    }

### 2. Get the average weather information for the specified period

#### 2.1 Request

`POST /api/v1/weather-data/average` with body:

    {
        "from": "16-03-2023",
        "to": "18-03-2023"
    }

#### 2.1 Response

    "timeStamp": "2023-03-22T13:24:30.3867172",
    "statusCode": 200,
    "status": "OK",
    "message": "Average weather data retrieved",
    "data": {
    "avg_data": {
        "avg_temp": 4.0,
        "avg_wind_kph": 10.75,
        "avg_pressure_mb": 1021.5,
        "avg_humidity": 52.5
        }
    }  

#### 2.2 Request with wrong body

`POST /api/v1/weather-data/average` with body:

    {
        "from": "",
        "to": ""
    }

#### 2.2 Response

    "timeStamp": "2023-03-22T13:34:11.2341081",
    "statusCode": 400,
    "status": "BAD_REQUEST",
    "message": "Dates shouldn't be empty!"

#### 2.3 Request with wrong body

`POST /api/v1/weather-data/average` with body:

    {
        "from": "22-03-2023",
        "to": "20-03-2023"
    }

#### 2.3 Response

    "timeStamp": "2023-03-22T13:36:34.2702664",
    "statusCode": 400,
    "status": "BAD_REQUEST",
    "message": "'From' date is greater than 'to' date"


