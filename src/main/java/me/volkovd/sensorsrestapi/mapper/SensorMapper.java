package me.volkovd.sensorsrestapi.mapper;

import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.models.Sensor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SensorMapper {

    private final ModelMapper modelMapper;

    public SensorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SensorDTO toDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

    public Sensor toModel(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

}
