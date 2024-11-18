package com.openbook.openbook.service.booth.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BoothReservationUpdateData(
        String name,
        String description,
        LocalDate date,
        String image,
        Integer price
) {
}
