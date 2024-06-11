package com.vg.controller.builder;

import com.github.javafaker.Faker;
import com.vg.model.WeatherData;

public class WeatherDataBuilder {
    private String description;
    private static final Faker faker = new Faker();

    public WeatherDataBuilder() {
        this.description = faker.weather().description();
    }

    public WeatherDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WeatherData build() {
        WeatherData weatherData = new WeatherData();
        weatherData.setDescription(this.description);
        return weatherData;
    }
}


