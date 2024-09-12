package com.openbook.openbook.service.booth.dto;


import jakarta.validation.constraints.NotNull;


public record BoothAreaCreateData(
        @NotNull String classification,
        @NotNull int maxNumber
) {
}
