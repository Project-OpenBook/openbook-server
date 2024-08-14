package com.openbook.openbook.booth.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BoothReservationDTO(
        String content,
        LocalDate date
) {
}
