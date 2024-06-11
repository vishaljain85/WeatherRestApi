package com.vg.controller;

import com.vg.controller.builder.WeatherTheoryBuilder;
import com.vg.exception.RateLimitExceededException;
import com.vg.exception.WeatherServiceException;
import com.vg.model.ErrorModel;
import com.vg.repository.WeatherRepository;
import com.vg.security.ApiKeyService;
import com.vg.service.WeatherService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherControllerTest {

    @InjectMocks
    WeatherController weatherController;

    @Mock
    WeatherService weatherService;

    @Mock
    WeatherRepository weatherRepository;

    @Mock
    ApiKeyService apiKeyService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    @DisplayName("When weather of location is in the database, it does not invokes Open Weather Map API")
    public void getWeatherFromDb() {
        String apiKey = "key1";
        String city = "Melbourne";
        String country = "AU";
        String location = city+","+country;

        WeatherTheoryBuilder theoryBuilder = new WeatherTheoryBuilder();

        when(weatherRepository.findById(location)).thenReturn(Optional.ofNullable(theoryBuilder.buildWeatherStore()));
        when(weatherService.getWeather(location)).thenReturn(theoryBuilder.buildOpenWeatherCurrent());

        ResponseEntity<?> response = weatherController.getWeather(city, country, apiKey);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(theoryBuilder.buildWeatherData(), response.getBody());
        verify(weatherService, times(0)).getWeather(location);
    }

    @SneakyThrows
    @Test
    @DisplayName("When weather of city is not in the database, it invokes Open Weather Map API")
    public void getWeatherFromOpenWeatherMapApi() {
        String apiKey = "key1";
        String city = "Melbourne";
        String country = "AU";
        String location = city+","+country;

        WeatherTheoryBuilder theoryBuilder = new WeatherTheoryBuilder();

        when(weatherRepository.findById(location)).thenReturn(null);
        when(weatherService.getWeather(location)).thenReturn(theoryBuilder.buildOpenWeatherCurrent());

        ResponseEntity<?> response = weatherController.getWeather(city, country, apiKey);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(theoryBuilder.buildWeatherData(), response.getBody());
        verify(weatherService, times(1)).getWeather(location);
    }

    @SneakyThrows
    @Test
    @DisplayName("When city is not found, it returns not found error")
    public void getWeatherCityNotFound() {

        String apiKey = "key1";
        String city = "Melbourne";
        String country = "AU";
        String location = city+","+country;

        String errorMessage = "city not found";

        when(weatherRepository.findById(location)).thenReturn(null);
        doThrow(new WeatherServiceException(errorMessage, errorMessage))
                .when(weatherService).getWeather(location);

        ErrorModel errorModel = new ErrorModel();
        errorModel.setErrorMessage(errorMessage);

        ResponseEntity<?> response = weatherController.getWeather(city, country, apiKey);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(errorModel, response.getBody());
        verify(weatherService, times(1)).getWeather(location);
    }

    @SneakyThrows
    @Test
    @DisplayName("When get weather reports exceeds limit of 5 calls in an hour, it returns rate limit error")
    public void getWeatherRateLimitError() {
        String apiKey = "key1";
        String errorMessage = "Hourly limit exceeded";

        doThrow(new RateLimitExceededException())
                .when(apiKeyService).validateApiKey(apiKey);

        ErrorModel errorModel = new ErrorModel();
        errorModel.setErrorMessage(errorMessage);
        ResponseEntity<?> response = weatherController.getWeather("a", "b", apiKey);

        assertEquals(429, response.getStatusCodeValue());
        assertEquals(errorModel, response.getBody());
    }
}
