package com.openbook.openbook.service.booth.dto;

import java.time.LocalDate;
import java.util.List;

public record BoothReservationDateDto(
        LocalDate date,
        List<BoothReservationDetailDto> times
) {
    public static BoothReservationDateDto of(LocalDate date, List<BoothReservationDetailDto> times) {
        return new BoothReservationDateDto(date, times);
    }
}
