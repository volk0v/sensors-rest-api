package me.volkovd.sensorsrestapi.mapper;

import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.models.Measurement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MeasurementMapper {

    private final ModelMapper modelMapper;

    public MeasurementMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MeasurementDTO toDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    public Measurement toModel(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

}
