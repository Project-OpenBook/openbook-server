package com.openbook.openbook.api.booth.request;

import com.openbook.openbook.domain.booth.dto.BoothNoticeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record BoothNoticeRegisterRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull BoothNoticeType noticeType,
        MultipartFile image
) {
}
