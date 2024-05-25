package com.openbook.openbook.basicuser.dto;


import jakarta.validation.constraints.NotNull;


public record LayoutAreaData(
        @NotNull String classification,
        @NotNull int maxNumber
) {
}
