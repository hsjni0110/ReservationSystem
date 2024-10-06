package com.example.reservationsystem.vehicle;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.vehicle.application.RouteService;
import com.example.reservationsystem.vehicle.dto.RouteCreateRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleCreateRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleRequest;
import com.example.reservationsystem.vehicle.dto.RouteScheduleResponse;
import com.example.reservationsystem.vehicle.presentation.RouteController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
@DisplayName("경로 컨트롤러(RouteController)는")
public class RouteControllerTest extends ControllerTest {

    @MockBean
    private RouteService routeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 출발지와_목적지에_따른_시간대를_생성할_수_있다() throws Exception {
        // given
        RouteCreateRequest routeCreateRequest = new RouteCreateRequest(
                "대전복합터미널",
                "센트럴시티",
                LocalDate.of(2024, 10, 9),
                List.of(
                        new RouteCreateRequest.TimeSlot("10:00"),
                        new RouteCreateRequest.TimeSlot("11:00"),
                        new RouteCreateRequest.TimeSlot("13:00"),
                        new RouteCreateRequest.TimeSlot("15:00"),
                        new RouteCreateRequest.TimeSlot("17:00"),
                        new RouteCreateRequest.TimeSlot("19:00")
                )
        );
        willDoNothing().given(routeService).createRoute(any(String.class), any(String.class), any(LocalDate.class), any(List.class));

        // when, then
        mockMvc.perform(post("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(routeCreateRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void 생성된_시간대에_버스를_배차할_수_있다() throws Exception {
        // given
        RouteScheduleCreateRequest request = new RouteScheduleCreateRequest(1L, 1L, 15000L,"10:00");
        willDoNothing().given(routeService).dispatchBus(any(Long.class), any(Long.class), any(String.class), any(Long.class));

        // when, then
        mockMvc.perform(post("/route/dispatch/bus")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    
    @Test
    void 출발지와_목적지에_따라_비어있는_경로_당_시간대_목록을_조회할_수_있다() throws Exception {
        // given
        RouteScheduleRequest routeTimeRequest = new RouteScheduleRequest("대전복합터미널", "센트럴시티", LocalDate.of(2024, 10, 10));
        RouteScheduleResponse routeTimeResponse = new RouteScheduleResponse(1L, 1L, "10:00");
        given(routeService.getAvailableRouteSchedules(any(String.class), any(String.class), any(LocalDate.class))).willReturn(List.of(routeTimeResponse));

        // when, then
        mockMvc.perform(post("/route/route-schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(routeTimeRequest)))
                .andExpect(jsonPath("$[0].routeScheduleId").value(1L))
                .andExpect(jsonPath("$[0].busId").value(1L))
                .andExpect(jsonPath("$[0].timeSlot").value("10:00"));
    }

}
