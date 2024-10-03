package com.openbook.openbook.service.booth.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoothUpdateData(
        String name,
        String description,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        String mainImage,
        String accountBankName,
        String accountNumber
) {
}
