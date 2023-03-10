package me.volkovd.sensorsrestapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.mapper.SensorMapper;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.SensorsService;
import me.volkovd.sensorsrestapi.validators.SensorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorsController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class SensorsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SensorMapper mapper;

    @MockBean
    private SensorValidator validator;

    @MockBean
    private SensorsService service;

    @BeforeEach
    public void setUp() {
        given(mapper.toModel(any())).willAnswer(invocationOnMock -> {
            SensorDTO sensorDTO = invocationOnMock.getArgument(0);
            return new Sensor(sensorDTO.getName());
        });
    }

    @Test
    public void givenCorrectRequest_whenPostRegistration_thenSensorSaved() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("test-sensor");
        String requestBody = new ObjectMapper().writeValueAsString(sensorDTO);

        mvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(service, times(1)).save(any());
    }

    @Test
    public void givenWrongRequest_whenPostRegistration_thenReturnError() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("a");
        String requestBody = new ObjectMapper().writeValueAsString(sensorDTO);

        mvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.errors[0].fieldName", is("name")),
                        jsonPath("$.errors[0].error", is("Name should be between 3 and 30 characters")),
                        jsonPath("$", hasKey("timestamp"))
                );

        verify(service, times(0)).save(any());
    }

    @Test
    public void givenExistingSensorName_whenPostRegistration_thenReturnError() throws Exception {
        String name = "test";
        SensorDTO sensorDTO = new SensorDTO(name);
        String requestBody = new ObjectMapper().writeValueAsString(sensorDTO);

        doAnswer(invocationOnMock -> {
            Errors errors = invocationOnMock.getArgument(1);
            errors.rejectValue("name", "", "Sensor with the name already exists");
            return null;
        }).when(validator).validate(any(), any());

        mvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.errors[0].fieldName", is("name")),
                        jsonPath("$.errors[0].error", is("Sensor with the name already exists")),
                        jsonPath("$", hasKey("timestamp"))
                );

        verify(service, times(0)).save(any());
    }

}