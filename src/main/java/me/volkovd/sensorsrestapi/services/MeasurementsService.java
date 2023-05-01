package me.volkovd.sensorsrestapi.services;

import me.volkovd.sensorsrestapi.exceptions.measurements.MeasurementNotAddedException;
import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.repositories.MeasurementsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementsService {

    private final MeasurementsRepository measurementRepository;
    private final SensorsService sensorsService;

    public MeasurementsService(MeasurementsRepository measurementRepository, SensorsService sensorsService) {
        this.measurementRepository = measurementRepository;
        this.sensorsService = sensorsService;
    }

    @Transactional(readOnly = true)
    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Measurement> findById(int id) {
        return measurementRepository.findById(id);
    }

    @Transactional
    public void save(Measurement measurement) {
        String sensorName = measurement.getSensor().getName();
        Sensor sensor = sensorsService.findByName(sensorName).orElseThrow(() -> new MeasurementNotAddedException("Sensor with the name doesn't exist!"));

        measurement.setSensor(sensor);

        measurement.setCreatedAt(LocalDateTime.now());

        measurementRepository.save(measurement);
    }

}
