package com.reservationsystem.vehicle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservationsystem.support.ControllerTest;
import com.system.reservationsystem.controller.BusController;
import com.system.vehicle.application.BusService;
import com.system.vehicle.application.dto.BusCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BusController.class)
@DisplayName("버스 컨트롤러(BusController)는")
public class BusControllerTest extends ControllerTest {

    @MockBean
    private BusService busService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 버스를_생성할_수_있다() throws Exception {
        // given
        willReturn(1L).given(busService).createBus(any(String.class), any(String.class), any(Integer.class));
        BusCreateRequest busCreateRequest = new BusCreateRequest("강남고속", "123", 30);

        // when, then
        mockMvc.perform(post("/bus")
                .content(objectMapper.writeValueAsString(busCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

}
