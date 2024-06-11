package com.vg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vg.exception.WeatherServiceException;
import com.vg.service.model.OpenWeatherCurrent;
import com.vg.service.model.OpenWeatherErrorResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${open-weather-map.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @SneakyThrows
    @Override
    public OpenWeatherCurrent getWeather(String location) throws WeatherServiceException{
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("q", location)
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            return restTemplate.getForObject(url, OpenWeatherCurrent.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            String responseString = httpClientErrorException.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            try {
                OpenWeatherErrorResponse errorResponse = mapper.readValue(responseString, OpenWeatherErrorResponse.class);
                throw new WeatherServiceException(httpClientErrorException.getResponseBodyAsString(),
                        errorResponse.getMessage());
            } catch (JsonProcessingException e) {
                throw new WeatherServiceException(e.getMessage(), "Internal Server Error");
            }
        } catch (Exception e) {
            throw new WeatherServiceException(e.getMessage(), "Internal Server Error");
        }
    }

}