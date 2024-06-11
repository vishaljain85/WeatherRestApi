package com.vg.controller.builder;

import com.github.javafaker.Faker;
import com.vg.repository.model.WeatherStore;

public class WeatherStoreBuilder {
    private String location;
    private String description;

    private static final Faker faker = new Faker();

    public WeatherStoreBuilder() {
        this.location = faker.address().city();
        this.description = faker.weather().description();
    }

    public WeatherStoreBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public WeatherStoreBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WeatherStore build() {
        WeatherStore weatherStore = new WeatherStore();
        weatherStore.setLocation(this.location);
        weatherStore.setDescription(this.description);
        return weatherStore;
    }
}

