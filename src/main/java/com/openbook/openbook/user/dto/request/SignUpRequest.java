package com.openbook.openbook.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @Email @NotNull String email,
        @NotNull String password,
        @NotNull String name,
        @NotNull String nickname
) {
}
