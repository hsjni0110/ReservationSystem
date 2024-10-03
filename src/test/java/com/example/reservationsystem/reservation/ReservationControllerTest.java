package com.example.reservationsystem.reservation;

import com.example.reservationsystem.common.ControllerTest;
import com.example.reservationsystem.reservation.application.ReservationService;
import com.example.reservationsystem.reservation.dto.ScheduledSeatResponse;
import com.example.reservationsystem.reservation.presentation.ReservationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("예약 컨트롤러(ReservationController)는")
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest extends ControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 예약_가능한_좌석을_조회할_수_있다() throws Exception {
        // given
        LocalDate specificDate = LocalDate.of(2024, 10, 10);
        String departure = "대전복합터미널";
        String arrival = "센트럴시티";
        String timeSlot = "10:00";

        ScheduledSeatResponse scheduledSeatResponse = new ScheduledSeatResponse(1L, 10, false);
        given(reservationService.getSeatsByRoute(departure, arrival, specificDate, timeSlot)).willReturn(List.of(scheduledSeatResponse));

        // when, then
        mockMvc.perform(get("/reservation/seats")
                        .param("specificDate", specificDate.toString())
                        .param("departure", departure)
                        .param("arrival", arrival)
                        .param("timeSlot", timeSlot))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scheduledSeatId").value(scheduledSeatResponse.scheduledSeatId()))
                .andExpect(jsonPath("$[0].seatId").value(scheduledSeatResponse.seatId()))
                .andExpect(jsonPath("$[0].isReserved").value(scheduledSeatResponse.isReserved()));
    }
}
