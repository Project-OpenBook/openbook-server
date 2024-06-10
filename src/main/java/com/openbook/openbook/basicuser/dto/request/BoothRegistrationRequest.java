package com.openbook.openbook.basicuser.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoothRegistrationRequest(
        @NotBlank String name,
        @NotNull Long linkedEvent,
        @NotNull String openTime,
        @NotNull String closeTime,
        @NotNull MultipartFile mainImage,
        @NotNull @Size(min = 1, max = 3) List<Long> layoutAreas,
        @NotBlank String description,
        String accountBankName,
        String accountNumber

        ) {
}
