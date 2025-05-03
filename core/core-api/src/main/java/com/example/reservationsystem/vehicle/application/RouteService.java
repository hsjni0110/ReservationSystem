package com.example.reservationsystem.vehicle.application;

import com.example.reservationsystem.common.config.CacheConfig;
import com.example.reservationsystem.vehicle.domain.model.Bus;
import com.example.reservationsystem.vehicle.domain.model.Route;
import com.example.reservationsystem.vehicle.domain.model.RouteSchedule;
import com.example.reservationsystem.vehicle.infra.repository.BusRepository;
import com.example.reservationsystem.vehicle.infra.repository.RouteRepository;
import com.example.reservationsystem.vehicle.infra.repository.RouteScheduleRepository;
import com.example.reservationsystem.vehicle.application.dto.RouteScheduleResponse;
import com.example.reservationsystem.vehicle.exception.VehicleException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    public Long createRoute(String departure, String arrival, LocalDate scheduleDate, List<String> times) {
        routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, scheduleDate)
                .ifPresent(route -> {
                    throw new VehicleException(DUPLICATE_ROUTE);
                });
        Route route = Route.create(departure, arrival, scheduleDate, times);
        return routeRepository.save(route).getRouteId();
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

    @Cacheable(
            cacheNames = CacheConfig.FIVE_MIN_CACHE,
            key = "'available-route-schedule'",
            condition = "#departure != null && #arrival != null && #scheduleDate != null",
            sync = true
    )
    public List<RouteScheduleResponse> getAvailableRouteSchedules(String departure, String arrival, LocalDate scheduleDate) {
        Route route = routeRepository.findByDepartureAndArrivalAndScheduleDate(departure, arrival, scheduleDate).orElseThrow(() -> new VehicleException(ROUTE_NOT_FOUND));
        List<RouteSchedule> routeSchedules = routeScheduleRepository.findByRouteTimeSlotIn(route.getRouteTimeSlots());
        return routeSchedules.stream()
                .filter(RouteSchedule::isAvailableSeats)
                .map(routeSchedule -> new RouteScheduleResponse(routeSchedule.getRouteScheduleId(), routeSchedule.getBus().getBusId(), routeSchedule.getTimeSlot()))
                .toList();
    }

}
