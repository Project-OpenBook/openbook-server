package com.openbook.openbook.service.booth.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.service.user.dto.UserDto;


public record BoothReservationDetailDto(
        long id,
        String times,
        BoothReservationStatus status,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UserDto applyUser
) {
    public static BoothReservationDetailDto of(BoothReservationDetail detail){
        return new BoothReservationDetailDto(
                detail.getId(),
                detail.getTime(),
                detail.getStatus(),
                detail.getUser() != null ? UserDto.of(detail.getUser()) : null
        );
    }
}
