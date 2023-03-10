package me.volkovd.sensorsrestapi.controllers;

import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.exceptions.sensors.SensorNotRegisteredException;
import me.volkovd.sensorsrestapi.mapper.SensorMapper;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.SensorsService;
import me.volkovd.sensorsrestapi.validators.SensorValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorMapper mapper;
    private final SensorValidator validator;

    private final SensorsService service;

    public SensorsController(SensorMapper mapper, SensorValidator validator, SensorsService service) {
        this.mapper = mapper;
        this.validator = validator;
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO,
                                               BindingResult bindingResult) {
        Sensor sensor = mapper.toModel(sensorDTO);
        validator.validate(sensor, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new SensorNotRegisteredException(bindingResult);
        }

        service.save(sensor);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
