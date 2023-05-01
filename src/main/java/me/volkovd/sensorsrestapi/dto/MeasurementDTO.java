package me.volkovd.sensorsrestapi.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MeasurementDTO {

    @NotEmpty(message = "Value field can't be empty")
    @Min(value = -100, message = "Value should be between -100 and 100")
    @Max(value = 100, message = "Value should be between -100 and 100")
    private float value;

    @NotNull(message = "Raining field can't be null")
    private boolean raining;

    @NotNull(message = "Sensor can't be null")
    private SensorDTO sensor;

    public MeasurementDTO() {
    }

    public MeasurementDTO(float value, boolean raining, SensorDTO sensor) {
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementDTO that = (MeasurementDTO) o;
        return Float.compare(that.value, value) == 0 && raining == that.raining && Objects.equals(sensor, that.sensor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, raining, sensor);
    }

}
