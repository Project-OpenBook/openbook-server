package com.openbook.openbook.booth.controller.request;

import jakarta.validation.constraints.NotBlank;

public record ProductCategoryRegister(
        @NotBlank String name,
        String description
) {
}
