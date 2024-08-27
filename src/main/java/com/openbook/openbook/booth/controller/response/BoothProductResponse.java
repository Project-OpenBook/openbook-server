package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.dto.ProductImageDto;
import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.entity.BoothProductImage;
import java.util.List;

public record BoothProductResponse(
        long id,
        String name,
        String description,
        int stock,
        int price,
        List<ProductImageDto> images
) {
    public static BoothProductResponse of(final BoothProduct product, List<BoothProductImage> images) {
        return new BoothProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                images.stream().map(ProductImageDto::of).toList()
        );
    }
}
