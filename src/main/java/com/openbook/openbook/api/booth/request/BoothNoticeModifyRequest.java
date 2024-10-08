package com.openbook.openbook.api.booth.request;

import com.openbook.openbook.domain.booth.dto.BoothNoticeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.web.multipart.MultipartFile;

public record BoothNoticeModifyRequest(
        String title,
        String content,
        @Enumerated(EnumType.STRING)
        BoothNoticeType noticeType,
        MultipartFile image
) {
}
