package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public record BoothReservationDetailResponse(
        long id,
        String times,
        @Enumerated(EnumType.STRING)
        BoothReservationStatus status
) {
    public static BoothReservationDetailResponse of(BoothReservationDetail detail){
        return new BoothReservationDetailResponse(
                detail.getId(),
                detail.getTime(),
                detail.getStatus()
        );
    }
}
