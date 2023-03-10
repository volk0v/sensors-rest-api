package me.volkovd.sensorsrestapi.controllers;

import me.volkovd.sensorsrestapi.dto.SensorDTO;
import me.volkovd.sensorsrestapi.mapper.SensorMapper;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.SensorsService;
import me.volkovd.sensorsrestapi.validators.SensorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

}