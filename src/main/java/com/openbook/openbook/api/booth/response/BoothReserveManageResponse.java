package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothReservationDateDto;
import com.openbook.openbook.service.booth.dto.BoothReservationDto;
import com.openbook.openbook.util.Formatter;
import java.util.List;

public record BoothReserveManageResponse(
        long id,
        String name,
        String description,
        int price,
        String imageUrl,
        List<BoothReservationDateDto> dates
) {
    public static BoothReserveManageResponse of(BoothReservationDto reservation){
        return new BoothReserveManageResponse(
                reservation.id(),
                reservation.name(),
                reservation.description(),
                reservation.price(),
                reservation.imageUrl(),
                reservation.groupedDetails().entrySet().stream()
                        .map(entry -> new BoothReservationDateDto(Formatter.getFormattingDate(entry.getKey().atStartOfDay()), entry.getValue()))
                        .toList()
        );
    }
}
