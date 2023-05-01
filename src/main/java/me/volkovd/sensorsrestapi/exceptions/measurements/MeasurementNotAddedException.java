package me.volkovd.sensorsrestapi.exceptions.measurements;

import me.volkovd.sensorsrestapi.exceptions.ValidatingException;
import org.springframework.validation.BindingResult;

public class MeasurementNotAddedException extends ValidatingException {

    public MeasurementNotAddedException(String message) {
        super(message);
    }

    public MeasurementNotAddedException(BindingResult bindingResult) {
        super(bindingResult);
    }

}
