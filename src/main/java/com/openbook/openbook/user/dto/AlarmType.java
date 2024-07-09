package com.openbook.openbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    EVENT("행사"),
    EVENT_REQUEST("행사 등록이 요청되었습니다."),
    BOOTH("부스");

    private final String description;
}
