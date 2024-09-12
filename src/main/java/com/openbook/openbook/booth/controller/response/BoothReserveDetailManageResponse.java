package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.user.controller.response.UserPublicResponse;
import com.openbook.openbook.user.dto.UserDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record BoothReserveDetailManageResponse(
        long id,
        String times,
        @Enumerated(EnumType.STRING)
        BoothReservationStatus status,
        UserPublicResponse reserveUser
) {
    public static BoothReserveDetailManageResponse of(BoothReservationDetail detail){
        return new BoothReserveDetailManageResponse(
                detail.getId(),
                detail.getTime(),
                detail.getStatus(),
                detail.getUser() != null ? UserPublicResponse.of(UserDto.of(detail.getUser())) : null
        );
    }
}
