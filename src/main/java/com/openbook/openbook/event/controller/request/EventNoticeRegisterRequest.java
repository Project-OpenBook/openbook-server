package com.openbook.openbook.event.controller.request;

import com.openbook.openbook.event.entity.dto.EventNoticeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EventNoticeRegisterRequest(
        @NotBlank String title,
        @NotBlank String content,
        @Enumerated(EnumType.STRING)
        @NotNull EventNoticeType noticeType,
        MultipartFile image
) {
}
