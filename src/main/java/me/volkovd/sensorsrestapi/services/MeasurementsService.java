package me.volkovd.sensorsrestapi.services;

import me.volkovd.sensorsrestapi.exceptions.measurements.MeasurementNotAddedException;
import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.repositories.MeasurementsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MeasurementsService {

    private final MeasurementsRepository measurementRepository;
    private final SensorsService sensorsService;

    public MeasurementsService(MeasurementsRepository measurementRepository, SensorsService sensorsService) {
        this.measurementRepository = measurementRepository;
        this.sensorsService = sensorsService;
    }

    @Transactional
    public int getRainyDaysAmount() {
        List<Measurement> measurements = findAll();
        Map<LocalDate, Integer> rainyDays = new HashMap<>();

        for (Measurement measurement : measurements) {
            if (measurement.isRaining()) {
                LocalDateTime createdAt = measurement.getCreatedAt();
                LocalDate date = createdAt.toLocalDate();

                rainyDays.put(date, rainyDays.getOrDefault(date, 0) + 1);
            }
        }

        return rainyDays.keySet().size();
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
