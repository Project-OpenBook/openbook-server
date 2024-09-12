package com.openbook.openbook.booth.service.dto;

import com.openbook.openbook.booth.entity.BoothProductImage;

public record ProductImageDto(
        long id,
        String url
) {
    public static ProductImageDto of(BoothProductImage image) {
        return new ProductImageDto(
                image.getId(),
                image.getImageUrl()
        );
    }
}
