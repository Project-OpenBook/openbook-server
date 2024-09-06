package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.user.dto.UserPublicData;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record BoothReserveDetailManageResponse(
        long id,
        String times,
        @Enumerated(EnumType.STRING)
        BoothReservationStatus status,
        UserPublicData reserveUser
) {
    public static BoothReserveDetailManageResponse of(BoothReservationDetail detail){
        UserPublicData userPublicData = detail.getUser() != null ? UserPublicData.of(detail.getUser()) : null;
        return new BoothReserveDetailManageResponse(
                detail.getId(),
                detail.getTime(),
                detail.getStatus(),
                userPublicData
        );
    }
}
