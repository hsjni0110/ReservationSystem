package com.system.domain.model;

import com.system.converter.MoneyConverter;
import com.system.domain.BaseEntity;
import com.system.domain.Money;
import com.system.exception.AccountException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.system.exception.AccountExceptionType.AMOUNT_IS_NOT_SUFFICIENT;

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
    private Long userId;

    @Getter
    @Convert( converter = MoneyConverter.class )
    private Money amount;

    @Version
    private Long version;

    public Account( Long accountId, Long userId, Money amount ) {
        this.accountId = accountId;
        this.amount = amount;
        this.version = 1L;
    }

    public Account( Long userId ) {
        this.userId = userId;
        this.version = 1L;
        this.amount = Money.ZERO;
    }

    public void deposit( Money payPrice ) {
        if (this.amount.isLessThan( payPrice )) {
            throw new AccountException( AMOUNT_IS_NOT_SUFFICIENT );
        }
        this.amount = this.amount.subtract( payPrice);
    }

    public Money recharge( Money chargeAmount ) {
        this.amount = this.amount.add( chargeAmount );
        return this.amount;
    }

}
