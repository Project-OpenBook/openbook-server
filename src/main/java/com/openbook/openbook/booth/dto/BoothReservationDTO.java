package com.openbook.openbook.booth.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record BoothReservationDTO(
        String name,
        String description,
        LocalDate date,
        MultipartFile image,
        int price
) {
}
