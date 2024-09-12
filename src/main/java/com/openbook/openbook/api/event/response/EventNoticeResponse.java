package com.openbook.openbook.api.event.response;

import com.openbook.openbook.service.event.dto.EventNoticeDto;
import com.openbook.openbook.domain.event.dto.EventNoticeType;
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
