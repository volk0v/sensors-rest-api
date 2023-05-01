package me.volkovd.sensorsrestapi.services;

import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.repositories.MeasurementsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementsService {

    private final MeasurementsRepository measurementRepository;

    public MeasurementsService(MeasurementsRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
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
        measurementRepository.save(measurement);
    }

}
