package com.openbook.openbook.api.event.request;

import com.openbook.openbook.domain.event.dto.EventNoticeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.web.multipart.MultipartFile;

public record EventNoticeModifyRequest(
        String title,
        String content,
        @Enumerated(EnumType.STRING)
        EventNoticeType noticeType,
        MultipartFile image
) {
}
