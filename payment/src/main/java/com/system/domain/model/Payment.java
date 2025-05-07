package com.system.domain.model;

import com.system.converter.MoneyConverter;
import com.system.domain.BaseEntity;
import com.system.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.system.reservation.domain.Reservation;
import com.system.type.PaymentStatus;
import com.system.user.signup.domain.model.User;

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

    public Payment( User user, Reservation reservation, Money totalPrice, PaymentStatus paymentStatus) {
        this.user = user;
        this.reservation = reservation;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }

    public void completePayment() {
        this.paymentStatus = PaymentStatus.PAYED;
    }

    public static Payment notPaidPayment(User user, Reservation reservation, Money totalPrice) {
        return new Payment(user, reservation, totalPrice, PaymentStatus.NOT_PAYED);
    }

    public void cancelPayment() {
        this.paymentStatus = PaymentStatus.PAY_CANCELED;
    }

    public void attemptPayment() {
        this.paymentStatus = PaymentStatus.NOT_PAYED;
    }

}
