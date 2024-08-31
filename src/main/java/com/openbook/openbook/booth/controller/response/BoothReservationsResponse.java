package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import com.openbook.openbook.booth.entity.BoothReservation;

import java.util.List;


public record BoothReservationsResponse(
        long id,
        String name,
        String description,
        String imageUrl,
        int price,
        String date,
        List<BoothReservationDetailResponse> details
) {
    public static BoothReservationsResponse of(BoothReservation boothReservation, List<BoothReservationDetailResponse> boothReservationDetail){
        return new BoothReservationsResponse(
                boothReservation.getId(),
                boothReservation.getName(),
                boothReservation.getDescription(),
                boothReservation.getImageUrl(),
                boothReservation.getPrice(),
                getFormattingDate(boothReservation.getDate().atStartOfDay()),
                boothReservationDetail
        );

    }
}
