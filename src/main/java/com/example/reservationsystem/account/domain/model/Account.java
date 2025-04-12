package com.example.reservationsystem.account.domain.model;

import com.example.reservationsystem.common.domain.model.BaseEntity;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.payment.exception.PaymentException;
import com.example.reservationsystem.common.converter.MoneyConverter;
import com.example.reservationsystem.user.signup.domain.model.User;
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
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Getter
    private Long accountId;

    @OneToOne
    @JoinColumn
    private User user;

    @Getter
    @Convert( converter = MoneyConverter.class )
    private Money amount;

    @Version
    private Long version;

    public Account( Long accountId, User user, Money amount ) {
        this.accountId = accountId;
        this.user = user;
        this.amount = amount;
        this.version = 1L;
    }

    public Account( User user ) {
        this.user = user;
        this.version = 1L;
        this.amount = Money.ZERO;
    }

    public void deposit( Money payPrice ) {
        if (this.amount.isLessThan( payPrice )) {
            throw new PaymentException( AMOUNT_IS_NOT_SUFFICIENT );
        }
        this.amount = this.amount.subtract( payPrice);
    }

    public Money recharge( Money chargeAmount ) {
        this.amount = this.amount.add( chargeAmount );
        return this.amount;
    }

}
