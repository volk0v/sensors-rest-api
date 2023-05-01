package me.volkovd.sensorsrestapi.validators;

import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.services.SensorsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {

    private final SensorsService sensorsService;

    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;

        String sensorName = measurement.getSensor().getName();
        if (!sensorsService.existsByName(sensorName)) {
            errors.rejectValue("sensor", "", "Sensor with the name doesn't exist!");
        }
    }

}
