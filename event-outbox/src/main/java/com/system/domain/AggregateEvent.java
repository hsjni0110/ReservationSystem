package com.system.domain;

import com.system.type.EventStatus;
import com.system.type.EventType;

import java.time.LocalDateTime;

/**
 * 이벤트는 특정 회원, 특정 행위(EventType), 특정 속성 변화(EventStatus), 특정 시간 정보, 그 외 페이로드를 가지고 있다.
 */
public interface AggregateEvent {

    // 특정 식별자는 해당 애그리게잇 도메인의 식별자를 반환
    Long getAggregateId();
    EventType getEventType();
    EventStatus getEventStatus();
    LocalDateTime getEventDate();

}
