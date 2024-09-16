package com.openbook.openbook.api.booth.request;

import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import jakarta.validation.constraints.NotNull;

public record ReserveStatusUpdateRequest(
        @NotNull BoothReservationStatus status
        ) {
}
