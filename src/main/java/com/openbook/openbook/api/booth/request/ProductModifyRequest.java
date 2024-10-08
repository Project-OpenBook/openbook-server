package com.openbook.openbook.api.booth.request;

public record ProductModifyRequest(
        String name,
        String description,
        Integer stock,
        Integer price,
        Long categoryId
) {
}
