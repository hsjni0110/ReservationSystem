package com.system.point.domain.model;

import com.system.converter.MoneyConverter;
import com.system.domain.BaseEntity;
import com.system.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PointHistory extends BaseEntity {

    public enum PointType { EARNED, SPENT }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Getter
    private Long pointHistoryId;

    @Getter
    private Long userId;

    @Column( length = 50 )
    @Enumerated( EnumType.STRING )
    @Getter
    private PointType source;

    @Convert( converter = MoneyConverter.class )
    @Getter
    private Money earnedPoints;

    public PointHistory( Long userId, Money earnedPoints ) {
        this.userId = userId;
        this.earnedPoints = earnedPoints;
        this.source = PointType.EARNED;
    }

    public static PointHistory earn( Long userId, Money earnedPoints ) {
        return new PointHistory( userId, earnedPoints );
    }

}
