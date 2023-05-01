package me.volkovd.sensorsrestapi.controllers;

import me.volkovd.sensorsrestapi.dto.MeasurementDTO;
import me.volkovd.sensorsrestapi.mapper.MeasurementMapper;
import me.volkovd.sensorsrestapi.models.Measurement;
import me.volkovd.sensorsrestapi.models.Sensor;
import me.volkovd.sensorsrestapi.services.MeasurementsService;
import me.volkovd.sensorsrestapi.validators.MeasurementValidator;
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
            return new Measurement(dto.getValue(), dto.isRaining(), new Sensor(dto.getSensor().getName()));
        });
    }

}