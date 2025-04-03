package com.example.reservationsystem.user.point.domain;

import com.example.reservationsystem.common.domain.Money;
import com.example.reservationsystem.payment.infra.MoneyConverter;
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
    private String transactionId;

    @Column( length = 50 )
    @Enumerated( EnumType.STRING )
    private PointType source;

    @Convert( converter = MoneyConverter.class )
    private Money earnedPoints ;

    public PointHistory( Long userId, String transactionId, Money earnedPoints ) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.earnedPoints = earnedPoints;
    }

    public static PointHistory earn( Long userId, String transactionId, Money earnedPoints ) {
        return new PointHistory( userId, transactionId, earnedPoints );
    }

}
