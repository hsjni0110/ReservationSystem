package com.system.vehicle.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RouteScheduleResponse {

    private Long routeScheduleId;
    private Long busId;
    private String timeSlot;

}
