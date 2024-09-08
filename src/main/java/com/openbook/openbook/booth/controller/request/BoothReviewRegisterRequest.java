package com.openbook.openbook.booth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record BoothReviewRegisterRequest(
        @NotNull long booth_id,
        @NotNull float star,
        @NotBlank String content,
        @NotNull MultipartFile image
        ) {
}
