package me.volkovd.sensorsrestapi.services;

import me.volkovd.sensorsrestapi.exceptions.sensors.SensorNotFoundException;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.repositories.SensorsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SensorsService {

    private final SensorsRepository sensorsRepository;

    public SensorsService(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        return sensorsRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Sensor> findAll() {
        return sensorsRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Sensor> findById(int id) {
        return sensorsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Sensor> findByName(String name) {
        return sensorsRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorsRepository.save(sensor);
    }

    @Transactional
    public void update(int id, Sensor sensor) {
        throwExceptionIfNotExistsById(id);

        sensor.setId(id);
        sensorsRepository.save(sensor);
    }

    @Transactional
    public void deleteById(int id) {
        throwExceptionIfNotExistsById(id);

        sensorsRepository.deleteById(id);
    }

    private void throwExceptionIfNotExistsById(int id) {
        if (!existsById(id)) {
            throw new SensorNotFoundException("Sensor with the ID wasn't found");
        }
    }

}
