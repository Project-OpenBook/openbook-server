package com.openbook.openbook.eventmanager.dto;

import com.openbook.openbook.booth.dto.BoothStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record BoothStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull BoothStatus boothStatus
        ) {
}
