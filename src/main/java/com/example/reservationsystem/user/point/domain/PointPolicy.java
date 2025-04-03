package com.example.reservationsystem.user.point.domain;

import com.example.reservationsystem.common.domain.Money;

public interface PointPolicy {

    Money calculatePoints( Money paymentAmount );

}
