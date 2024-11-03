package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothReservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record BoothReservationDto(
        long id,
        String name,
        String description,
        String imageUrl,
        int price,
        Map<LocalDate, List<BoothReservationDetailDto>> groupedDetails
) {
    public static BoothReservationDto of(BoothReservation boothReservation) {
        return new BoothReservationDto(
                boothReservation.getId(),
                boothReservation.getName(),
                boothReservation.getDescription(),
                boothReservation.getImageUrl(),
                boothReservation.getPrice(),
                boothReservation.getBoothReservationDetails().stream()
                        .collect(Collectors.groupingBy(
                                detail -> detail.getLinkedReservation().getDate(),
                                Collectors.mapping(BoothReservationDetailDto::of, Collectors.toList())
                        ))
        );
    }
}
