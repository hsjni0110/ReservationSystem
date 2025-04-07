package com.example.reservationsystem.common.domain.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Builder
public class Money implements Comparable<Money> {

    public static final Money ZERO = Money.wons(0);

    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    public Money(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor cannot be negative");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public int compareTo(Money other) {
        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

}
