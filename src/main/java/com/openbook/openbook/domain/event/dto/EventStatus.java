package com.openbook.openbook.domain.event.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {
    WAITING("승인 대기"),
    APPROVE("승인 완료"),
    REJECT("승인 거부");

    private final String description;
}
