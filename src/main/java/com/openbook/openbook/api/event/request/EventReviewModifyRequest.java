package com.openbook.openbook.api.event.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@NotNull
public record EventReviewModifyRequest(
        @Max(value = 5) Float star,
        String content,
        @Max(value = 5) List<MultipartFile> images
) {
}
