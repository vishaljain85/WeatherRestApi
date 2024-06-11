package com.vg.controller;

import com.vg.exception.InvalidApiKeyException;
import com.vg.exception.RateLimitExceededException;
import com.vg.exception.WeatherServiceException;
import com.vg.model.ErrorModel;
import com.vg.model.WeatherData;
import com.vg.repository.WeatherRepository;
import com.vg.repository.model.WeatherStore;
import com.vg.security.ApiKeyService;
import com.vg.service.WeatherService;
import com.vg.service.model.OpenWeatherCurrent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;

    private ApiKeyService apiKeyService;

    @Autowired
    private final WeatherRepository weatherRepository;

    public WeatherController(
            WeatherService weatherService, ApiKeyService apiKeyService, WeatherRepository weatherRepository) {
        this.weatherService = weatherService;
        this.apiKeyService = apiKeyService;
        this.weatherRepository = weatherRepository;
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(
            @RequestParam("city") String city, @RequestParam("country") String country,
            @RequestHeader(value = "API-Key", required = false) String apiKey
    ){
        try {
            this.apiKeyService.validateApiKey(apiKey);
            String location = city+","+country;
            WeatherData weatherData = null;
            Optional<WeatherStore> weatherStore = this.weatherRepository.findById(location);
            if(weatherStore != null && !weatherStore.isEmpty()) {
                weatherData = new WeatherData();
                weatherData.setDescription(weatherStore.get().getDescription());
            } else {
                OpenWeatherCurrent openWeatherCurrent = this.weatherService.getWeather(location);
                if (openWeatherCurrent.getWeather() != null && openWeatherCurrent.getWeather().length > 0) {
                    String weatherDescription = openWeatherCurrent.getWeather()[0].getDescription();
                    WeatherStore store = new WeatherStore();
                    store.setLocation(location);
                    store.setDescription(weatherDescription);
                    this.weatherRepository.save(store);
                    weatherData = new WeatherData();
                    weatherData.setDescription(weatherDescription);
                }
            }
            return ResponseEntity.ok(weatherData);
        } catch (InvalidApiKeyException e) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorMessage("Invalid API Key");
            return new ResponseEntity<>(errorModel, HttpStatus.UNAUTHORIZED);
        } catch (RateLimitExceededException e) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorMessage("Hourly limit exceeded");
            return new ResponseEntity<>(errorModel, HttpStatus.TOO_MANY_REQUESTS);
        } catch (WeatherServiceException e) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setErrorMessage(e.getErrorMessage());
            return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
        }
    }

}



