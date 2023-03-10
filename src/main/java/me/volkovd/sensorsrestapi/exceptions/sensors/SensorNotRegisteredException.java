package me.volkovd.sensorsrestapi.exceptions.sensors;

import me.volkovd.sensorsrestapi.utils.FieldErrors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class SensorNotRegisteredException extends RuntimeException {

    private BindingResult bindingResult;

    public SensorNotRegisteredException(String message) {
        super(message);
    }

    public SensorNotRegisteredException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public List<FieldErrors> getErrors() {
        List<FieldErrors> errors = new ArrayList<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add(new FieldErrors(error.getField(), error.getDefaultMessage()));
        }

        return errors;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
