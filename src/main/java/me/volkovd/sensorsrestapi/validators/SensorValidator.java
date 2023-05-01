package me.volkovd.sensorsrestapi.validators;

import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.SensorsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {

    private final SensorsService sensorsService;

    public SensorValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if (sensorsService.existsByName(sensor.getName())) {
            errors.rejectValue("name", "", "Sensor with the name already exists");
        }
    }

}
