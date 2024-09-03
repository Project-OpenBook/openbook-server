package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import com.openbook.openbook.booth.entity.BoothReservation;

import java.util.List;


public record BoothReservationsResponse(
        long id,
        String name,
        String description,
        String date,
        List<BoothReservationDetailResponse> details,
        int price,
        String imageUrl,
        long boothManagerId
) {
    public static BoothReservationsResponse of(BoothReservation boothReservation, List<BoothReservationDetailResponse> boothReservationDetail){
        return new BoothReservationsResponse(
                boothReservation.getId(),
                boothReservation.getName(),
                boothReservation.getDescription(),
                getFormattingDate(boothReservation.getDate().atStartOfDay()),
                boothReservationDetail,
                boothReservation.getPrice(),
                boothReservation.getImageUrl(),
                boothReservation.getLinkedBooth().getManager().getId()
        );

    }
}
