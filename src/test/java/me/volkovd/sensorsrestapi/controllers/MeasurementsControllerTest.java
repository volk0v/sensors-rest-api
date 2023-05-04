package me.volkovd.sensorsrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.mapper.MeasurementMapper;
import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.MeasurementsService;
import me.volkovd.sensorsrestapi.validators.MeasurementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementsController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class MeasurementsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MeasurementsService service;

    @MockBean
    private MeasurementMapper mapper;

    @MockBean
    private MeasurementValidator validator;

    @BeforeEach
    public void setUp() {
        given(mapper.toModel(any())).willAnswer(invocationOnMock -> {
            MeasurementDTO dto = invocationOnMock.getArgument(0);
            return new ModelMapper().map(dto, Measurement.class);
        });

        doAnswer(onMock -> {
            Measurement measurement = onMock.getArgument(0);
            if (measurement.getSensor() == null || !measurement.getSensor().getName().equals("WRONG")) return null;

            Errors errors = onMock.getArgument(1);
            errors.rejectValue("sensor", "", "Sensor with the name doesn't exist!");
            return null;
        }).when(validator).validate(any(), any());
    }

    @Test
    public void givenCorrectRequest_whenPostAdd_thenSaved() throws Exception {
        MeasurementDTO dto = new MeasurementDTO(24.7f, false, new SensorDTO("Sensor name"));
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(post("/measurements/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(service, times(1)).save(any());
    }

    @ParameterizedTest
    @MethodSource("wrongRequestsProvider")
    public void givenWrongField_whenPostAdd_thenReturnError(WrongRequest errorRequest) throws Exception {
        mvc.perform(post("/measurements/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(errorRequest.getBody()))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        hasErrorForField(errorRequest.getField(), errorRequest.getError()),
                        jsonPath("$", hasKey("timestamp"))
                );

        verify(service, times(0)).save(any());
    }

    @Test
    public void givenThreeMeasurements_whenGetAll_thenReturnJson() throws Exception {
        givenThreeMeasurementsInDatabase();
        mockMapperToMapDTOsList();

        mvc.perform(get("/measurements/"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$", hasSize(3)),
                        checkJsonOfMeasurement(24.7f, true, "House"),
                        checkJsonOfMeasurement(-24.7f, false, "Street"),
                        checkJsonOfMeasurement(0f, false, "Underground")
                );
    }

    @Test
    public void givenFiveRainyDays_whenGetRainyDays_thenReturnFive() throws Exception {
        when(service.getRainyDaysAmount()).thenReturn(5);

        mvc.perform(get("/measurements/rainy-days-amount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(5)));
    }

    private void givenThreeMeasurementsInDatabase() {
        Measurement measurement1 = new Measurement(24.7f, true, new Sensor("House"));
        measurement1.setCreatedAt(LocalDate.of(2023, 5, 1).atStartOfDay());
        Measurement measurement2 = new Measurement(-24.7f, false, new Sensor("Street"));
        measurement2.setCreatedAt(LocalDate.of(2023, 5, 2).atStartOfDay());
        Measurement measurement3 = new Measurement(0f, false, new Sensor("Underground"));
        measurement3.setCreatedAt(LocalDate.of(2023, 5, 3).atStartOfDay());

        doAnswer(onMock -> Arrays.asList(measurement1, measurement2, measurement3))
                .when(service).findAll();
    }

    private void mockMapperToMapDTOsList() {
        given(mapper.toListOfDTOs(any())).willAnswer(invocationOnMock -> {
            List<Measurement> models = invocationOnMock.getArgument(0);
            List<MeasurementDTO> result = new ArrayList<>();

            ModelMapper mapper = new ModelMapper();
            for (Measurement measurement : models) {
                result.add(mapper.map(measurement, MeasurementDTO.class));
            }

            return result;
        });
    }

    private static Stream<WrongRequest> wrongRequestsProvider() {
        return Stream.of(
                new WrongRequest("{\"value\": 1000,\"raining\":false,\"sensor\": {\"name\": \"CORRECT\"}}",
                        "value", "Value should be between -100 and 100"),
                new WrongRequest("{\"raining\":false,\"sensor\": {\"name\": \"CORRECT\"}}",
                        "value", "Value field can't be null"),
                new WrongRequest("{\"value\": 24.7,\"sensor\": {\"name\": \"CORRECT\"}}",
                        "raining", "Raining field can't be null"),
                new WrongRequest("{\"value\": 24.7,\"raining\":false}",
                        "sensor", "Sensor can't be null"),
                new WrongRequest("{\"value\": 24.7,\"raining\":false,\"sensor\": {\"name\": \"WRONG\"}}",
                        "sensor", "Sensor with the name doesn't exist!")
        );
    }

    private ResultMatcher checkJsonOfMeasurement(float value, boolean raining, String sensorName) {
        return result -> {
            jsonPath("$[?(@.value == " + value + ")].raining").value(raining).match(result);
            jsonPath("$[?(@.value == " + value + ")].sensor.name").value(sensorName).match(result);
        };
    }

    private ResultMatcher hasErrorForField(String fieldName, String error) {
        return result -> {
            jsonPath("$.errors[?(@.fieldName == '" + fieldName + "')].fieldName").value(fieldName).match(result);
            jsonPath("$.errors[?(@.fieldName == '" + fieldName + "')].error").value(error).match(result);
        };
    }

}