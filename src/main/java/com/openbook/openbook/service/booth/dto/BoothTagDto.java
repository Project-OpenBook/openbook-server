package com.openbook.openbook.service.booth.dto;


import com.openbook.openbook.domain.booth.BoothTag;

public record BoothTagDto(
        long id,
        String name
) {
    public static BoothTagDto of(BoothTag boothTag) {
        return new BoothTagDto(
                boothTag.getId(),
                boothTag.getName()
        );
    }
}
