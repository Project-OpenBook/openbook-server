package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.global.dto.SliceResponse;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record CategoryProductsResponse(
        ProductCategoryResponse category,
        SliceResponse<BoothProductResponse> products
) {
    public static CategoryProductsResponse of(BoothProductCategory category, Slice<BoothProductResponse> products) {
        return new CategoryProductsResponse(
                ProductCategoryResponse.of(category),
                SliceResponse.of(products)
        );
    }
}
