package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothReservation;
import java.time.LocalDate;
import java.util.List;

public record BoothReservationDto(
        long id,
        String name,
        String description,
        String imageUrl,
        int price,
        LocalDate date,
        List<BoothReservationDetailDto> details
) {
    public static BoothReservationDto of(BoothReservation boothReservation) {
        return new BoothReservationDto(
                boothReservation.getId(),
                boothReservation.getName(),
                boothReservation.getDescription(),
                boothReservation.getImageUrl(),
                boothReservation.getPrice(),
                boothReservation.getDate(),
                boothReservation.getBoothReservationDetails().stream().map(BoothReservationDetailDto::of).toList()
        );
    }
}
