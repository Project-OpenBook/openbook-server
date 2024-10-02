package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoothModifyRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull String openTime,
        @NotNull String closeTime,
        @NotNull MultipartFile mainImage,
        List<String> tagToAdd,
        List<Long> tagToDelete,
        String accountBankName,
        String accountNumber
        ) {
}
