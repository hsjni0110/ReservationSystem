package com.example.reservationsystem.reservation;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.reservation.application.ReservationService;
import com.example.reservationsystem.reservation.dto.AvailableSeatRequest;
import com.example.reservationsystem.reservation.dto.SeatReservationRequest;
import com.example.reservationsystem.reservation.dto.SeatReservationResponse;
import com.example.reservationsystem.reservation.presentation.ReservationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("예약 컨트롤러(ReservationController)는")
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest extends ControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        사용자_인가();
    }

    @Test
    void 예약_가능한_좌석을_조회할_수_있다() throws Exception {
        // given
        AvailableSeatRequest availableSeatRequest = new AvailableSeatRequest(1L);
        ScheduledSeatResponse scheduledSeatResponse = new ScheduledSeatResponse(1L, 10, false);
        given(reservationService.getSeatsByRoute(availableSeatRequest.routeScheduleId())).willReturn(List.of(scheduledSeatResponse));

        // when, then
        mockMvc.perform(get("/reservation/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availableSeatRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scheduledSeatId").value(scheduledSeatResponse.scheduledSeatId()))
                .andExpect(jsonPath("$[0].seatId").value(scheduledSeatResponse.seatId()))
                .andExpect(jsonPath("$[0].isReserved").value(scheduledSeatResponse.isReserved()));
    }

    @Test
    void 특정_날짜의_특정_TimeSlot에_비어있는_자리를_점유할_수_있다() throws Exception {
        // given
        Long userId = 1L;
        Long routeScheduleId = 1L;
        Long scheduledSeatId = 1L;
        Long scheduledSeatId2 = 1L;
        Long reservationId = 1L;

        // when
        SeatReservationRequest seatReservationRequest = new SeatReservationRequest(routeScheduleId, List.of(scheduledSeatId, scheduledSeatId2));
        SeatReservationResponse seatReservationResponse = new SeatReservationResponse(reservationId, List.of(scheduledSeatId, scheduledSeatId2));
        given(reservationService.preserveSeat(userId, routeScheduleId, List.of(scheduledSeatId, scheduledSeatId2))).willReturn(seatReservationResponse);

        // then
        mockMvc.perform(post("/reservation/seat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seatReservationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservationId));
    }

}
