package me.volkovd.sensorsrestapi.controllers;

import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.exceptions.measurements.MeasurementNotAddedException;
import me.volkovd.sensorsrestapi.mapper.MeasurementMapper;
import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.services.MeasurementsService;
import me.volkovd.sensorsrestapi.utils.FieldsErrorsResponse;
import me.volkovd.sensorsrestapi.validators.MeasurementValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService service;
    private final MeasurementMapper mapper;
    private final MeasurementValidator validator;


    public MeasurementsController(MeasurementsService service, MeasurementMapper mapper, MeasurementValidator validator) {
        this.service = service;
        this.mapper = mapper;
        this.validator = validator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@Valid @RequestBody MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        Measurement measurement = mapper.toModel(measurementDTO);
        validator.validate(measurement, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new MeasurementNotAddedException(bindingResult);
        }

        service.save(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<MeasurementDTO> get() {
        return mapper.toListOfDTOs(service.findAll());
    }

    @ExceptionHandler
    private ResponseEntity<FieldsErrorsResponse> handleException(MeasurementNotAddedException exception) {
        FieldsErrorsResponse response = new FieldsErrorsResponse(
                exception.getErrors(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
