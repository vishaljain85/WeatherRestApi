package com.vg.controller.builder;

import com.github.javafaker.Faker;
import com.vg.service.model.OpenWeatherCurrent;
import com.vg.service.model.OpenWeatherDescription;

import java.util.stream.Stream;

public class OpenWeatherCurrentBuilder {
    private OpenWeatherDescription[] weather;

    private Faker faker = new Faker();


    public OpenWeatherCurrentBuilder() {
        int numDescriptions = faker.number().numberBetween(1, 10);
        this.weather = Stream.generate(OpenWeatherDescriptionBuilder::new)
                .limit(numDescriptions)
                .map(OpenWeatherDescriptionBuilder::build)
                .toArray(OpenWeatherDescription[]::new);
    }

    public OpenWeatherCurrentBuilder withWeather(OpenWeatherDescription[] weather) {
        this.weather = weather;
        return this;
    }

    public OpenWeatherCurrent build() {
        OpenWeatherCurrent openWeatherCurrent = new OpenWeatherCurrent();
        openWeatherCurrent.setWeather(this.weather);
        return openWeatherCurrent;
    }
}

