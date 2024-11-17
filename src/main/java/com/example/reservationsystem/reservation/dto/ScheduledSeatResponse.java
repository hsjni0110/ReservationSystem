package com.example.reservationsystem.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduledSeatResponse {

    private Long scheduleSeatId;
    private Integer seatId;
    private boolean isReserved;

}
