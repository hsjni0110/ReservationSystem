package com.system.point.domain.model;

import com.system.converter.MoneyConverter;
import com.system.domain.BaseEntity;
import com.system.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long pointId;
    @Getter
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
