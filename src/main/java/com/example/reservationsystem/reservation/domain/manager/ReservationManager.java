package com.example.reservationsystem.reservation.domain.manager;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.domain.repository.ReservationRepository;
import com.example.reservationsystem.reservation.domain.repository.ScheduledSeatRepository;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.user.signup.domain.User;
import com.example.reservationsystem.user.signup.domain.UserRepository;
import com.example.reservationsystem.vehicle.domain.repository.RouteScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.ALREADY_PRESERVED_SEAT;

@Component
public class ReservationManager {

    private static final Long RESERVATION_EXPIRATION_TIME = 5L;

    private final UserRepository userRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCacheManager reservationCacheManager;

    public ReservationManager(UserRepository userRepository, ScheduledSeatRepository scheduledSeatRepository, ReservationRepository reservationRepository, RouteScheduleRepository routeScheduleRepository, ReservationCacheManager reservationCacheManager) {
        this.userRepository = userRepository;
        this.scheduledSeatRepository = scheduledSeatRepository;
        this.reservationRepository = reservationRepository;
        this.reservationCacheManager = reservationCacheManager;
    }

    @Transactional
    public Reservation preserve(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
        User user = userRepository.getByIdOrThrow( userId );

        List<ScheduledSeat> scheduledSeats = scheduleSeatIds.stream()
                .map( scheduleSeatId -> scheduledSeatRepository.findByIdWithPessimisticLock( scheduleSeatId, routeScheduleId ))
                .toList();

        scheduledSeats.forEach(
                ScheduledSeat::reserveSeat
        );

        Reservation reservation = Reservation.from( user, scheduledSeats );
        evictCacheFromRouteSchedule( routeScheduleId );
        return reservationRepository.save( reservation );
    }

    private void evictCacheFromRouteSchedule(Long routeScheduleId) {
        reservationCacheManager.evictRouteScheduleCache();
        reservationCacheManager.evictRouteScheduleCache(routeScheduleId);
    }

    /*
     1. 만기된 예약 건 조회
     2. 조회된 예약 건 삭제
     3. 조회된 좌석의 상태 변경(이용 가능)
     4. 해당 일정의 좌석 수 및 상태 변경(필요 시)
     */
    public void cancelReservations() {
        var expirationTime = LocalDateTime.now().minusMinutes( RESERVATION_EXPIRATION_TIME );
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservation( Reservation.ReservationStatus.PENDING, expirationTime );

        if ( expiredReservations.isEmpty() ) {
            return;
        }

        expiredReservations.forEach( Reservation::cancelReservation );
        reservationRepository.deleteAll(expiredReservations);
    }

}
