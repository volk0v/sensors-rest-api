package me.volkovd.sensorsrestapi.services;

import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.repositories.MeasurementsRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MeasurementsServiceTest {

    @Mock
    private MeasurementsRepository repository;

    @Mock
    private SensorsService sensorsService;

    @ParameterizedTest
    @MethodSource("getRainyMeasurementsExamples")
    public void givenMeasurements_whenGetsRainyDays_thenReturnInts(List<Measurement> measurements, int expectedRainyDays) {
        when(repository.findAll()).thenReturn(measurements);
        MeasurementsService sus = new MeasurementsService(repository, sensorsService);

        int actual = sus.getRainyDaysAmount();

        assertThat(actual).isEqualTo(expectedRainyDays);
    }

    public static Stream<Arguments> getRainyMeasurementsExamples() {
        return Stream.of(
                Arguments.of( // 3 date, 2 raining measurements, 2 rainy days
                        Arrays.asList(
                                measurementOf(24.7f, true, "House", LocalDate.of(2023, 5, 1)),
                                measurementOf(-24.7f, false, "Street", LocalDate.of(2023, 1, 1)),
                                measurementOf(0.0f, true, "Underground", LocalDate.of(2023, 4, 15))
                        ),
                        2
                ),
                Arguments.of( // 3 date, 0 raining measurements, 0 rainy days
                        Arrays.asList(
                                measurementOf(24.7f, false, "House", LocalDate.of(2023, 5, 1)),
                                measurementOf(-24.7f, false, "Street", LocalDate.of(2023, 1, 1)),
                                measurementOf(0.0f, false, "Underground", LocalDate.of(2023, 4, 15))
                        ),
                        0
                ),
                Arguments.of( // 1 date, 2 raining measurements, 1 rainy day
                        Arrays.asList(
                                measurementOf(24.7f, true, "House", LocalDate.of(2023, 5, 1)),
                                measurementOf(-24.7f, false, "Street", LocalDate.of(2023, 5, 1)),
                                measurementOf(0.0f, true, "Underground", LocalDate.of(2023, 5, 1))
                        ),
                        1
                ),
                Arguments.of( // 0 date, 0 raining measurements, 0 rainy day
                        Collections.emptyList(),
                        0
                )
        );
    }

    private static Measurement measurementOf(float value, boolean raining, String sensorName, LocalDate localDate) {
        Measurement measurement = new Measurement(value, raining, new Sensor(sensorName));
        LocalDateTime createdAt = localDate.atStartOfDay();

        measurement.setCreatedAt(createdAt);

        return measurement;
    }

}