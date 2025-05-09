package com.system.point.domain.model;

import com.system.domain.Money;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class DefaultPointPolicy implements PointPolicy {

    private static final NavigableMap<Money, Double> POINT_RATE = new TreeMap<>( Map.of(
            Money.wons(0), 0.01,
            Money.wons( 10000 ), 0.02,
            Money.wons( 20000 ), 0.03,
            Money.wons( 30000 ), 0.04,
            Money.wons( 40000 ), 0.05
    ) );

    @Override
    public Money calculatePoints( Money paymentAmount ) {
        return Optional.ofNullable( POINT_RATE.floorEntry( paymentAmount ) )
                .map( entry -> paymentAmount.multiply( entry.getValue() ))
                .orElse( Money.wons( 0 ) );
    }

}
