package com.openbook.openbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    //EVENT
    EVENT_REQUEST_RECEIVED("행사 등록 요청이 들어왔습니다."),
    EVENT_REQUEST_APPROVE("행사 등록 요청이 승인되었습니다."),
    EVENT_REQUEST_REJECT("행사 등록 요청이 거부되었습니다."),

    //BOOTH
    BOOTH_REQUEST_RECEIVED("부스 등록 요청이 들어왔습니다."),
    BOOTH_REQUEST_APPROVE("부스 등록 요청이 승인되었습니다."),
    BOOTH_REQUEST_REJECT("부스 등록 요청이 거부되었습니다.");


    private final String description;
}
