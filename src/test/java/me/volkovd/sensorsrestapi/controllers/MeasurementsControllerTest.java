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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            return new Measurement(dto.getValue(), dto.isRaining(), new Sensor(dto.getSensor().getName()));
        });
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

}