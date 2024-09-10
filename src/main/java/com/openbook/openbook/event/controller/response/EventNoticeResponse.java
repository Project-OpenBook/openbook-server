package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.dto.EventNoticeDto;
import com.openbook.openbook.event.entity.dto.EventNoticeType;
import java.time.LocalDateTime;

public record EventNoticeResponse(
        long id,
        String title,
        String content,
        String imageUrl,
        EventNoticeType type,
        LocalDateTime registeredAt,
        EventPublicResponse event
) {
    public static EventNoticeResponse of(EventNoticeDto notice) {
        return new EventNoticeResponse(
                notice.id(),
                notice.title(),
                notice.content(),
                notice.imageUrl(),
                EventNoticeType.valueOf(notice.type()),
                notice.registeredAt(),
                EventPublicResponse.of(notice.linkedEvent())
        );
    }
}
