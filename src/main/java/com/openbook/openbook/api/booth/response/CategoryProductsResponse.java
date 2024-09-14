package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.domain.booth.BoothProductCategory;
import com.openbook.openbook.api.SliceResponse;
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
