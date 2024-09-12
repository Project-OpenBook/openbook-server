package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.service.dto.BoothReservationDto;
import java.util.List;

public record BoothReserveManageResponse(
        long id,
        String name,
        String description,
        String date,
        int price,
        String imageUrl,
        List<BoothReserveDetailManageResponse> detailManageResponses
) {
    public static BoothReserveManageResponse of(BoothReservationDto reservation){
        return new BoothReserveManageResponse(
                reservation.id(),
                reservation.name(),
                reservation.description(),
                getFormattingDate(reservation.date().atStartOfDay()),
                reservation.price(),
                reservation.imageUrl(),
                reservation.details().stream().map(BoothReserveDetailManageResponse::of).toList()
        );
    }
}
