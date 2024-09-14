package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothProductImageDto;
import com.openbook.openbook.domain.booth.BoothProduct;
import com.openbook.openbook.domain.booth.BoothProductImage;
import java.util.List;

public record BoothProductResponse(
        long id,
        String name,
        String description,
        int stock,
        int price,
        List<BoothProductImageDto> images
) {
    public static BoothProductResponse of(final BoothProduct product, List<BoothProductImage> images) {
        return new BoothProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                images.stream().map(BoothProductImageDto::of).toList()
        );
    }
}
