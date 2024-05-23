package com.openbook.openbook.booth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record BoothRegistrationRequest(
        @NotBlank String name,
        @NotNull Long linkedEvent,
        @NotNull LocalDateTime openTime,
        @NotNull LocalDateTime closeTime,
        @NotNull MultipartFile mainImage,
        @NotNull @Size(min = 1, max = 3) List<Long> locations,
        @NotBlank String description,
        String accountNumber

        ) {
}
