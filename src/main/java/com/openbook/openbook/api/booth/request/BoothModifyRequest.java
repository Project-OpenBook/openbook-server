package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BoothModifyRequest(
        String name,
        String description,
        String openTime,
        String closeTime,
        MultipartFile mainImage,
        List<String> tagToAdd,
        List<Long> tagToDelete,
        String accountBankName,
        String accountNumber
        ) {
}
