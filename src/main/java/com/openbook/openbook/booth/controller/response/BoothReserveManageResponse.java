package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import com.openbook.openbook.booth.entity.BoothReservation;

import java.util.List;

public record BoothReserveManageResponse(
        long id,
        String name,
        String description,
        String date,
        List<BoothReserveDetailManageResponse> detailManageResponses,
        int price,
        String imageUrl
) {
    public static BoothReserveManageResponse of(BoothReservation reservation,
                                                List<BoothReserveDetailManageResponse> detailManage){
        return new BoothReserveManageResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDescription(),
                getFormattingDate(reservation.getDate().atStartOfDay()),
                detailManage,
                reservation.getPrice(),
                reservation.getImageUrl()
        );
    }
}
