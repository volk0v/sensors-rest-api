package me.volkovd.sensorsrestapi.mapper;

import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.models.Measurement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MeasurementMapper {

    private final ModelMapper modelMapper;

    public MeasurementMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MeasurementDTO toDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    public List<MeasurementDTO> toListOfDTOs(List<Measurement> measurements) {
        List<MeasurementDTO> result = new ArrayList<>();

        for (Measurement measurement : measurements) {
            result.add(toDTO(measurement));
        }

        return result;
    }

    public Measurement toModel(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public List<Measurement> toListOfModels(List<MeasurementDTO> measurementsDTOs) {
        List<Measurement> result = new ArrayList<>();

        for (MeasurementDTO measurementDTO : measurementsDTOs) {
            result.add(toModel(measurementDTO));
        }

        return result;
    }

}
