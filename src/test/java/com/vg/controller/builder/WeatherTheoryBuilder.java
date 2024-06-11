package com.vg.controller.builder;

import com.github.javafaker.Faker;
import com.vg.model.WeatherData;
import com.vg.repository.model.WeatherStore;
import com.vg.service.model.OpenWeatherCurrent;
import com.vg.service.model.OpenWeatherDescription;

public class WeatherTheoryBuilder {
    private WeatherDataBuilder weatherDataBuilder;

    private WeatherStoreBuilder weatherStoreBuilder;

    private OpenWeatherCurrentBuilder openWeatherCurrentBuilder;

    private static final Faker faker = new Faker();

    public WeatherTheoryBuilder(){
        String description = faker.weather().description();

        OpenWeatherDescription openWeatherDescription = new OpenWeatherDescriptionBuilder()
                .withDescription(description)
                .build();
        OpenWeatherDescription[] openWeatherDescriptionList = new OpenWeatherDescription[1];
        openWeatherDescriptionList[0] = openWeatherDescription;

        this.weatherStoreBuilder = new WeatherStoreBuilder()
                .withLocation("Melbourne,AU")
                .withDescription(description);
        this.openWeatherCurrentBuilder = new OpenWeatherCurrentBuilder()
                .withWeather(openWeatherDescriptionList);
        this.weatherDataBuilder = new WeatherDataBuilder()
                .withDescription(description);
    }

    public WeatherData buildWeatherData() {
        return this.weatherDataBuilder.build();
    }

    public WeatherStore buildWeatherStore() {
        return this.weatherStoreBuilder.build();
    }

    public OpenWeatherCurrent buildOpenWeatherCurrent() {
        return this.openWeatherCurrentBuilder.build();
    }
}
