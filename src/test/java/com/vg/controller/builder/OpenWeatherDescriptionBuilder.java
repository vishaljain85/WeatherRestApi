package com.vg.controller.builder;

import com.github.javafaker.Faker;
import com.vg.service.model.OpenWeatherDescription;

public class OpenWeatherDescriptionBuilder {
    private String description;
    private static final Faker faker = new Faker();

    public OpenWeatherDescriptionBuilder() {
        this.description = faker.weather().description();
    }

    public OpenWeatherDescriptionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public OpenWeatherDescription build() {
        OpenWeatherDescription openWeatherDescription = new OpenWeatherDescription();
        openWeatherDescription.setDescription(this.description);
        return openWeatherDescription;
    }
}

