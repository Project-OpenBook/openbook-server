package com.openbook.openbook.booth.dto;


import jakarta.validation.constraints.NotNull;


public record BoothAreaCreateData(
        @NotNull String classification,
        @NotNull int maxNumber
) {
}
