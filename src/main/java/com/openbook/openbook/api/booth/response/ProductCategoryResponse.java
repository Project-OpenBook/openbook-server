package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.domain.booth.BoothProductCategory;

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
