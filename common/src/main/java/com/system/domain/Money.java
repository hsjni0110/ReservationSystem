package com.system.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Builder
public class Money implements Comparable<Money> {

    public static final Money ZERO = Money.wons(0);

    @Builder.Default
    private BigDecimal money = BigDecimal.ZERO;

    public Money(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.money = amount;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money add(Money other) {
        return new Money(this.money.add(other.money));
    }

    public Money subtract(Money other) {
        return new Money(this.money.subtract(other.money));
    }

    public Money multiply(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor cannot be negative");
        }
        return new Money(this.money.multiply(BigDecimal.valueOf(factor)));
    }

    public boolean isLessThan(Money other) {
        return money.compareTo(other.money) < 0;
    }

    public boolean isPositive() {
        return money.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getMoney() {
        return money;
    }

    @Override
    public int compareTo(Money other) {
        return this.money.compareTo(other.money);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return this.money.equals(money.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

}
