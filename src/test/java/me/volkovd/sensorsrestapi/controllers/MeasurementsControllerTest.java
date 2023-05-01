package me.volkovd.sensorsrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.mapper.MeasurementMapper;
import me.volkovd.sensorsrestapi.models.Measurement;
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

import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

    private ResultMatcher hasErrorForField(String fieldName, String error) {
        return result -> {
            jsonPath("$.errors[?(@.fieldName == '" + fieldName + "')].fieldName").value(fieldName).match(result);
            jsonPath("$.errors[?(@.fieldName == '" + fieldName + "')].error").value(error).match(result);
        };
    }

}