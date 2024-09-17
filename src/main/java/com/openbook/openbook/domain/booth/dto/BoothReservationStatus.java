package com.openbook.openbook.domain.booth.dto;

import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import lombok.Getter;

@Getter
public enum BoothReservationStatus {
    EMPTY,
    WAITING,
    COMPLETE;

    public static BoothReservationStatus fromString(String request){
        return switch (request){
            case "EMPTY" -> EMPTY;
            case "WAITING" -> WAITING;
            case "COMPLETE" -> COMPLETE;
            default -> throw new OpenBookException(ErrorCode.INVALID_RESERVATION_TYPE);
        };
    }
}
