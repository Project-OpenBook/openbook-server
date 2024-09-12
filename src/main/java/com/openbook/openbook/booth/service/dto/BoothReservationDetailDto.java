package com.openbook.openbook.booth.service.dto;

import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.user.dto.UserDto;


public record BoothReservationDetailDto(
        long id,
        String times,
        BoothReservationStatus status,
        UserDto applyUser
) {
    public static BoothReservationDetailDto of(BoothReservationDetail detail){
        return new BoothReservationDetailDto(
                detail.getId(),
                detail.getTime(),
                detail.getStatus(),
                UserDto.of(detail.getUser())
        );
    }
}
