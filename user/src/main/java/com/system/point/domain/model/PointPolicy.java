package com.system.point.domain.model;

import com.system.domain.Money;

public interface PointPolicy {

    Money calculatePoints(Money paymentAmount );

}
