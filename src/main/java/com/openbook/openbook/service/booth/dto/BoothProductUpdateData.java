package com.openbook.openbook.service.booth.dto;

import lombok.Builder;

@Builder
public record BoothProductUpdateData(
        String name,
        String description,
        Integer stock,
        Integer price
) {
}
