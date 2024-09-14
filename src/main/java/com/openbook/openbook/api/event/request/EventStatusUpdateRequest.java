package com.openbook.openbook.api.event.request;

import com.openbook.openbook.domain.event.dto.EventStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record EventStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull EventStatus status
) {
}
