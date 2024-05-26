package com.openbook.openbook.basicuser.dto;


import jakarta.validation.constraints.NotNull;


public record LayoutAreaCreateData(
        @NotNull String classification,
        @NotNull int maxNumber
) {
}
