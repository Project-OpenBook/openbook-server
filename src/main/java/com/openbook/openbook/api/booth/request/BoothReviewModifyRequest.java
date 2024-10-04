package com.openbook.openbook.api.booth.request;

import jakarta.validation.constraints.Max;
import org.springframework.web.multipart.MultipartFile;

public record BoothReviewModifyRequest(
        @Max(value = 5) Float star,
        String content,
        MultipartFile image
) {
}
