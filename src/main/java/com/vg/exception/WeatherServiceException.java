package com.vg.exception;

import lombok.Data;

@Data
public class WeatherServiceException extends Exception {
    private String errorMessage;

    public WeatherServiceException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }
}
