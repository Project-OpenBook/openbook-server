package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventNoticeType;

public record EventNoticeDto(
        String title,
        String content,
        String imageUrl,
        EventNoticeType type,
        Event linkedEvent
) {
}
