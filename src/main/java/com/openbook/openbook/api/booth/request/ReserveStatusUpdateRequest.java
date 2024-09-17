package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.NotNull;

public record ReserveStatusUpdateRequest(
        @NotNull String status
        ) {
}
