package com.vg.repository;

import com.vg.repository.model.WeatherStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherStore, String> {

}
