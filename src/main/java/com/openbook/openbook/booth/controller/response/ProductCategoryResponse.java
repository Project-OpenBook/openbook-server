package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothProductCategory;

public record ProductCategoryResponse(
        long id,
        String name,
        String description
) {

    public static ProductCategoryResponse of(BoothProductCategory category) {
        return new ProductCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
