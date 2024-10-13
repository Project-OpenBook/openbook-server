package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record ReserveRegistrationRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull int price,
        MultipartFile image,
        @NotNull List<LocalDate> dates,
        @NotEmpty List<String> times
        ) {
}
