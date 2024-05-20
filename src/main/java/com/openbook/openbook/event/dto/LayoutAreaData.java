package com.openbook.openbook.event.dto;


import jakarta.validation.constraints.NotNull;


public record LayoutAreaData(
        @NotNull String classification,
        @NotNull int maxNumber
) {
}
