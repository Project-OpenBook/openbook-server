package com.openbook.openbook.administrator.dto.request;

import com.openbook.openbook.event.dto.EventStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record EventStatusUpdateRequest(
        @Enumerated(EnumType.STRING)
        @NotNull EventStatus status
) {
}
