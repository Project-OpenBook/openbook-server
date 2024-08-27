package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.BoothProduct;
import com.openbook.openbook.booth.entity.BoothProductCategory;
import com.openbook.openbook.global.dto.SliceResponse;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record CategoryProducts(
        ProductCategoryResponse category,
        SliceResponse<BoothProductResponse> products
) {
    public static CategoryProducts of(BoothProductCategory category, Slice<BoothProduct> products) {
        return new CategoryProducts(
                ProductCategoryResponse.of(category),
                SliceResponse.of(products.map(BoothProductResponse::of))
        );
    }
}
