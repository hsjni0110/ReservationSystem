package com.example.reservationsystem.user.point.domain.model;

import com.example.reservationsystem.common.domain.model.Money;

public interface PointPolicy {

    Money calculatePoints( Money paymentAmount );

}
