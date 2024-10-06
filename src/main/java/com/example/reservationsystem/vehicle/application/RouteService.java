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

import java.time.LocalDate;
import java.util.List;

import static com.example.reservationsystem.vehicle.exception.VehicleExceptionType.*;

@Service
@RequiredArgsConstructor
@RequestMapping
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteScheduleRepository routeScheduleRepository;
    private final BusRepository busRepository;

    @Transactional
    public void createRoute(String departure, String arrival, LocalDate scheduleDate, List<String> times) {
        routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, scheduleDate)
                .ifPresent(route -> {
                    throw new VehicleException(DUPLICATE_ROUTE);
                });
        Route route = Route.create(departure, arrival, scheduleDate, times);
        routeRepository.save(route);
    }

    @Transactional
    public void dispatchBus(Long routeId, Long busId, String timeSlot, long seatPrice) {
        Bus bus = busRepository.getByIdOrThrow(busId);
        Route route = routeRepository.getByIdOrThrow(routeId);
        routeScheduleRepository.findByBusAndRouteTimeSlot(bus, route.getMatchedRouteTimeSlot(timeSlot)).ifPresent(
                routeSchedule -> {
                    throw new VehicleException(DUPLICATE_DISPATCH_TIME);
                }
        );
        RouteSchedule routeSchedule = RouteSchedule.create(bus, route.getMatchedRouteTimeSlot(timeSlot), seatPrice);
        routeScheduleRepository.save(routeSchedule);
    }

    @Transactional(readOnly = true)
    public List<RouteScheduleResponse> getAvailableRouteSchedules(String departure, String arrival, LocalDate scheduleDate) {
        Route route = routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, scheduleDate).orElseThrow(() -> new VehicleException(ROUTE_NOT_FOUND));
        List<RouteSchedule> routeSchedules = routeScheduleRepository.findByRouteTimeSlotIn(route.getRouteTimeSlots());
        return routeSchedules.stream()
                .filter(RouteSchedule::isAvailableSeats)
                .map(routeSchedule -> new RouteScheduleResponse(routeSchedule.getRouteScheduleId(), routeSchedule.getBus().getBusId(), routeSchedule.getTimeSlot()))
                .toList();
    }

}
