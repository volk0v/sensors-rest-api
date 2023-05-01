package me.volkovd.sensorsrestapi.exceptions.sensors;

import me.volkovd.sensorsrestapi.exceptions.ValidatingException;
import org.springframework.validation.BindingResult;

public class SensorNotRegisteredException extends ValidatingException {

    public SensorNotRegisteredException(String message) {
        super(message);
    }

    public SensorNotRegisteredException(BindingResult bindingResult) {
        super(bindingResult);
    }

}
