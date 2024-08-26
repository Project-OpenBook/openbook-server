package com.openbook.openbook.booth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record ReserveRegistrationRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull int price,
        MultipartFile image,
        @NotNull LocalDate date,
        @NotEmpty List<String> times
        ) {
}
