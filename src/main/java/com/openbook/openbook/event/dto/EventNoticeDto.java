package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventNoticeType;
import org.springframework.web.multipart.MultipartFile;

public record EventNoticeDto(
        String title,
        String content,
        MultipartFile image,
        EventNoticeType type,
        Event linkedEvent
) {
}
