package com.openbook.openbook.api.event.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record EventReviewModifyRequest(
        @NotNull @Max(value = 5) float star,
        @NotBlank String content,
        List<MultipartFile> imageToAdd,
        List<Long> imageToDelete
) {
}
