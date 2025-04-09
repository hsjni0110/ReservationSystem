package com.example.reservationsystem.user.point.domain.model;

import com.example.reservationsystem.common.domain.model.Money;
import com.example.reservationsystem.common.converter.MoneyConverter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PointHistory {

    public enum PointType { EARNED, SPENT }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long pointHistoryId;

    private Long userId;

    @Column( length = 50 )
    @Enumerated( EnumType.STRING )
    private PointType source;

    @Convert( converter = MoneyConverter.class )
    private Money earnedPoints ;

    public PointHistory( Long userId, Money earnedPoints ) {
        this.userId = userId;
        this.earnedPoints = earnedPoints;
    }

    public static PointHistory earn( Long userId, Money earnedPoints ) {
        return new PointHistory( userId, earnedPoints );
    }

}
