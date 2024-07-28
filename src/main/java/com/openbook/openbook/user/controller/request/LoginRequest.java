package com.openbook.openbook.user.controller.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String email,
        @NotNull String password
) {
}
