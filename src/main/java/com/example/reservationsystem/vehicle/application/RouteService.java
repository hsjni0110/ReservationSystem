package com.example.reservationsystem.vehicle.application;

import com.example.reservationsystem.vehicle.domain.Bus;
import com.example.reservationsystem.vehicle.domain.Route;
import com.example.reservationsystem.vehicle.domain.RouteSchedule;
import com.example.reservationsystem.vehicle.domain.repository.BusRepository;
import com.example.reservationsystem.vehicle.domain.repository.RouteRepository;
import com.example.reservationsystem.vehicle.domain.repository.RouteScheduleRepository;
import com.example.reservationsystem.vehicle.dto.RouteScheduleResponse;
import com.example.reservationsystem.vehicle.exception.VehicleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.reservationsystem.vehicle.exception.VehicleExceptionType.ROUTE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@RequestMapping
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteScheduleRepository routeScheduleRepository;
    private final BusRepository busRepository;

    @Transactional
    public void createRoute(String departure, String arrival, List<String> times) {
        Route route = Route.create(departure, arrival, times);
        routeRepository.save(route);
    }

    @Transactional
    public void dispatchBus(Long routeId, Long busId, String timeSlot) {
        Bus bus = busRepository.getByIdOrThrow(busId);
        Route route = routeRepository.getByIdOrThrow(routeId);
        RouteSchedule routeSchedule = RouteSchedule.create(bus, route.getMatchedRouteTimeSlot(timeSlot));
        routeScheduleRepository.save(routeSchedule);
    }

    @Transactional(readOnly = true)
    public List<RouteScheduleResponse> getAvailableRouteSchedules(String departure, String arrival) {
        Route route = routeRepository.findByDepartureAndArrival(departure, arrival).orElseThrow(() -> new VehicleException(ROUTE_NOT_FOUND));
        List<RouteSchedule> routeSchedules = routeScheduleRepository.findByRouteTimeSlotIn(route.getRouteTimeSlots());
        return routeSchedules.stream()
                .filter(RouteSchedule::isAvailableSeats)
                .map(routeSchedule -> new RouteScheduleResponse(routeSchedule.getRouteScheduleId(), routeSchedule.getBus().getBusId(), routeSchedule.getTimeSlot()))
                .toList();
    }

}
