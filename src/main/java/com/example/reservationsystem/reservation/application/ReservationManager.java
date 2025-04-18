package com.example.reservationsystem.reservation.application;

import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.reservation.exception.ReservationException;
import com.example.reservationsystem.reservation.infra.repository.ReservationRepository;
import com.example.reservationsystem.reservation.infra.repository.ScheduledSeatRepository;
import com.example.reservationsystem.user.signup.domain.model.User;
import com.example.reservationsystem.user.signup.infra.repository.UserRepository;
import com.example.reservationsystem.vehicle.infra.repository.RouteScheduleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.reservationsystem.reservation.exception.ReservationExceptionType.ROUTE_SCHEDULE_NOT_FOUND;

@Component
public class ReservationManager {

    private static final Long RESERVATION_EXPIRATION_TIME = 5L;

    private final UserRepository userRepository;
    private final ScheduledSeatRepository scheduledSeatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationCacheManager reservationCacheManager;
    private final RouteScheduleRepository routeScheduleRepository;

    public ReservationManager(UserRepository userRepository, ScheduledSeatRepository scheduledSeatRepository, ReservationRepository reservationRepository, ReservationCacheManager reservationCacheManager, RouteScheduleRepository routeScheduleRepository) {
        this.userRepository = userRepository;
        this.scheduledSeatRepository = scheduledSeatRepository;
        this.reservationRepository = reservationRepository;
        this.reservationCacheManager = reservationCacheManager;
        this.routeScheduleRepository = routeScheduleRepository;
    }

    @Transactional
    public Reservation preserve(Long userId, Long routeScheduleId, List<Long> scheduleSeatIds ) {
        validate( routeScheduleId, scheduleSeatIds );
        User user = userRepository.getByIdOrThrow( userId );

        List<ScheduledSeat> scheduledSeats = scheduledSeatRepository.findAllByIdsWithPessimisticLock(scheduleSeatIds, routeScheduleId);

        Reservation reservation = Reservation.from( user, scheduledSeats );
        evictCacheFromRouteSchedule( routeScheduleId );
        return reservationRepository.save(reservation);
    }

    private void validate( Long routeScheduleId, List<Long> scheduleSeatId ) {
        validateRouteScheduleExist( routeScheduleId );
        validateReserveTime( routeScheduleId );
    }

    private void validateReserveTime( Long routeScheduleId ) {
        // 출발 시간 15분 이전인지 확인
    }

    private void validateRouteScheduleExist( Long routeScheduleId ) {
        routeScheduleRepository.findById( routeScheduleId )
                .orElseThrow(() -> new ReservationException( ROUTE_SCHEDULE_NOT_FOUND ));
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
        reservationRepository.deleteAll( expiredReservations );
    }

    public Long confirmReservation( Long reservationId ) {
        Reservation reservation = reservationRepository.getByIdOrThrow( reservationId );
        reservation.successPayment();
        return reservationId;
    }

}
