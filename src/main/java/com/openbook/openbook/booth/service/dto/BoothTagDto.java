package com.openbook.openbook.booth.service.dto;


import com.openbook.openbook.booth.entity.BoothTag;

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
