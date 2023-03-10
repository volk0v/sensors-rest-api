package me.volkovd.sensorsrestapi.exceptions.sensors;

public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException(String message) {
        super(message);
    }

}
