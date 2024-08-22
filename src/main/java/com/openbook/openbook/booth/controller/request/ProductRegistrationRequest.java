package com.openbook.openbook.booth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ProductRegistrationRequest(
        long categoryId,
        @NotBlank String name,
        String description,
        int stock,
        int price,
        @Size(max = 5) List<MultipartFile> images
) {
}
