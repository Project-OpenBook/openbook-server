package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.BoothProductImage;

public record BoothProductImageDto(
        long id,
        String url
) {
    public static BoothProductImageDto of(BoothProductImage image) {
        return new BoothProductImageDto(
                image.getId(),
                image.getImageUrl()
        );
    }
}
