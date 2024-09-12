package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.service.dto.BoothReservationDetailDto;
import com.openbook.openbook.booth.service.dto.BoothReservationDto;
import java.util.List;


public record BoothReserveResponse(
        long id,
        String name,
        String description,
        int price,
        String imageUrl,
        String date,
        List<BoothReservationDetailDto> details
) {
    public static BoothReserveResponse of(BoothReservationDto reservation){
        return new BoothReserveResponse(
                reservation.id(),
                reservation.name(),
                reservation.description(),
                reservation.price(),
                reservation.imageUrl(),
                getFormattingDate(reservation.date().atStartOfDay()),
                reservation.details()
        );
    }
}
