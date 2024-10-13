package com.example.reservationsystem.account.domain;

import com.example.reservationsystem.common.domain.BaseEntity;
import com.example.reservationsystem.payment.exception.PaymentException;
import com.example.reservationsystem.payment.infra.MoneyConverter;
import com.example.reservationsystem.user.signup.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.reservationsystem.payment.exception.PaymentExceptionType.AMOUNT_IS_NOT_SUFFICIENT;

@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @OneToOne
    @JoinColumn
    private User user;

    @Getter
    @Convert(converter = MoneyConverter.class)
    @Builder.Default
    private Money amount = Money.ZERO;

    public Account(User user) {
        this.user = user;
    }

    public void pay(Money payPrice) {
        if (this.amount.isLessThan(payPrice)) {
            throw new PaymentException(AMOUNT_IS_NOT_SUFFICIENT);
        }
        this.amount = this.amount.subtract(amount);
    }

    public Money recharge(Money chargeAmount) {
        this.amount = this.amount.add(chargeAmount);
        return this.amount;
    }

}
