package com.vg.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="city_weather")
public class WeatherStore {

    @Id
    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;
}
