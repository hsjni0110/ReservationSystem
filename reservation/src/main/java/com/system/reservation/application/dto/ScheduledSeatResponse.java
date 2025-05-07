package com.system.reservation.application.dto;

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
