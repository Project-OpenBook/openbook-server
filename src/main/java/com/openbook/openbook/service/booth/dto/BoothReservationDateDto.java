package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.util.Formatter;

import java.time.LocalDate;
import java.util.List;

public record BoothReservationDateDto(
        String date,
        List<BoothReservationDetailDto> times
) {
    public static BoothReservationDateDto of(LocalDate date, List<BoothReservationDetailDto> times) {
        return new BoothReservationDateDto(Formatter.getFormattingDate(date.atStartOfDay()), times);
    }
}
