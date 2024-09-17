package com.openbook.openbook.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    EVENT_REQUEST("행사 등록이 요청되었습니다."),
    BOOTH_REQUEST("부스 등록이 요청되었습니다."),

    EVENT_APPROVED("행사 등록이 승인되었습니다."),
    EVENT_REJECTED("행사 등록이 거부되었습니다."),

    BOOTH_APPROVED("부스 등록이 승인되었습니다."),
    BOOTH_REJECTED("부스 등록이 거부되었습니다."),

    RESERVE_APPROVED("예약 신청이 승인되었습니다."),
    RESERVE_REJECTED("예약 신청이 거부되었습니다.")

    ;

    private final String message;
}
