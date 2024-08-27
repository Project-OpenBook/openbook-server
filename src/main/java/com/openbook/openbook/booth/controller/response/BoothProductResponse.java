package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothProduct;

public record BoothProductResponse(
        long id,
        String name,
        String description,
        int stock,
        int price
) {
    public static BoothProductResponse of(final BoothProduct product) {
        return new BoothProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice()
        );
    }
}
