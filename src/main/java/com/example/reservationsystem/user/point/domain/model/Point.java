package com.example.reservationsystem.user.point.domain.model;

import com.example.reservationsystem.common.domain.model.BaseEntity;
import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.converter.MoneyConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;
    private Long userId;

    @Getter
    @Convert(converter = MoneyConverter.class)
    private Money totalPoints = Money.ZERO;

    protected Point(Long userId, Money totalPoints) {
        this.userId = userId;
        this.totalPoints = totalPoints;
    }

    public static Point of(Long userId ) {
        return new Point(userId, Money.ZERO);
    }

    public void addPoints(Money earnedPoints) {
        this.totalPoints = this.totalPoints.add(earnedPoints);
    }

}
