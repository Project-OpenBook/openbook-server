package com.openbook.openbook.booth.dto;

import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record BoothAreaStatusData(
        Long id,
        @Enumerated(EnumType.STRING)
        BoothAreaStatus status,
        String number
) {
}
