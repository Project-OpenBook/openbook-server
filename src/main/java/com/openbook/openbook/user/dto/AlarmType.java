package com.openbook.openbook.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    EVENT("행사"),
    BOOTH("부스");

    private final String description;
}
