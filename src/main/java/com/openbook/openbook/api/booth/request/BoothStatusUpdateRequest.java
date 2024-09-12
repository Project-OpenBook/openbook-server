package com.openbook.openbook.api.booth.request;

import com.openbook.openbook.domain.booth.dto.BoothStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record BoothStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull BoothStatus boothStatus
        ) {
}
