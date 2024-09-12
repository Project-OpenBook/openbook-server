package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothArea;

public record BoothAreaDto(
        long id,
        String classification,
        String number,
        String status
) {
    public static BoothAreaDto of(BoothArea boothArea){
        return new BoothAreaDto(
                boothArea.getId(),
                boothArea.getClassification(),
                boothArea.getNumber(),
                boothArea.getStatus().name()
        );
    }
}
