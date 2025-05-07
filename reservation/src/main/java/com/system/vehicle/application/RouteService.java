package com.system.vehicle.application;

import com.system.config.CacheConfig;
import com.system.vehicle.domain.model.Bus;
import com.system.vehicle.domain.model.Route;
import com.system.vehicle.domain.model.RouteSchedule;
import com.system.vehicle.infra.repository.BusRepository;
import com.system.vehicle.infra.repository.RouteRepository;
import com.system.vehicle.infra.repository.RouteScheduleRepository;
import com.system.vehicle.application.dto.RouteScheduleResponse;
import com.system.vehicle.exception.VehicleException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

import static com.system.vehicle.exception.VehicleExceptionType.*;

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
