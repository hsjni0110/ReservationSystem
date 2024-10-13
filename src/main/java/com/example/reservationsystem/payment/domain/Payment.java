package com.example.reservationsystem.payment.domain;

import com.example.reservationsystem.account.domain.Money;
import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.common.type.PaymentStatus;
import com.example.reservationsystem.payment.infra.MoneyConverter;
import com.example.reservationsystem.reservation.domain.Reservation;
import com.example.reservationsystem.reservation.domain.ScheduledSeat;
import com.example.reservationsystem.user.signup.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Reservation reservation;

    @Column(name = "total_price")
    @Getter
    @Convert(converter = MoneyConverter.class)
    private Money totalPrice;

    @Getter
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Payment(User user, Reservation reservation, Money totalPrice, PaymentStatus paymentStatus) {
        this.user = user;
        this.reservation = reservation;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }

    public static Payment successFrom(User user, Reservation reservation, Money totalPrice) {
        return new Payment(user, reservation, totalPrice, PaymentStatus.PAYED);
    }

}
