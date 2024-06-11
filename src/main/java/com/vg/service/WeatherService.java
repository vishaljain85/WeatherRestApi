package com.vg.service;

import com.vg.exception.WeatherServiceException;
import com.vg.service.model.OpenWeatherCurrent;

public interface WeatherService {
    OpenWeatherCurrent getWeather(String location) throws WeatherServiceException;
}
