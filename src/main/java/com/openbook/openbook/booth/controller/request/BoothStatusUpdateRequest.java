package com.openbook.openbook.booth.controller.request;

import com.openbook.openbook.booth.entity.dto.BoothStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record BoothStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull BoothStatus boothStatus
        ) {
}
