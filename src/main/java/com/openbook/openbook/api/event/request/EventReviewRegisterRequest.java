package com.openbook.openbook.api.event.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record EventReviewRegisterRequest(
        @NotNull long event_id,
        @NotNull float star,
        @NotBlank String content,
        List<MultipartFile> images
) {
}
