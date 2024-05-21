package com.openbook.openbook.booth.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record BoothCreateRequest(
        @NotBlank String name,
        @NotNull Long linkedEvent,
        @NotNull LocalDateTime openTime,
        @NotNull LocalDateTime closeTime,
        @NotNull MultipartFile mainImageUrl,
        @NotNull @Size(min = 1, max = 3) List<Long> locations,
        @NotNull String description,
        @NotNull String accountNumber

        ) {
}
