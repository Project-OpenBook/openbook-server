package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.service.booth.dto.BoothReservationDetailDto;
import com.openbook.openbook.api.user.response.UserPublicResponse;

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
