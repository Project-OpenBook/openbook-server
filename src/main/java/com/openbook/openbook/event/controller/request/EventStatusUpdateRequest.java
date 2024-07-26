package com.openbook.openbook.event.controller.request;

import com.openbook.openbook.event.entity.dto.EventStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record EventStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull EventStatus status
) {
}
