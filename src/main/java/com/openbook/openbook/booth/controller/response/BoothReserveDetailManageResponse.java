package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.booth.service.dto.BoothReservationDetailDto;
import com.openbook.openbook.user.controller.response.UserPublicResponse;

public record BoothReserveDetailManageResponse(
        long id,
        String times,
        BoothReservationStatus status,
        UserPublicResponse applyUser
) {
    public static BoothReserveDetailManageResponse of(BoothReservationDetailDto detail){
        return new BoothReserveDetailManageResponse(
                detail.id(),
                detail.times(),
                detail.status(),
                detail.applyUser() != null ? UserPublicResponse.of(detail.applyUser()) : null
        );
    }
}
