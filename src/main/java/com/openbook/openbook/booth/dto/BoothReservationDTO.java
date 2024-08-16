package com.openbook.openbook.booth.dto;

import java.time.LocalDate;

public record BoothReservationDTO(
        String content,
        LocalDate date
) {
}
