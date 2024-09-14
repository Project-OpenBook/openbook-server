package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.NotBlank;

public record ProductCategoryRegister(
        @NotBlank String name,
        String description
) {
}
