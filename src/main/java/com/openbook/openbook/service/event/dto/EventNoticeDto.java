package com.openbook.openbook.service.event.dto;


import com.openbook.openbook.domain.event.EventNotice;
import java.time.LocalDateTime;

public record EventNoticeDto(
        long id,
        String type,
        String title,
        String content,
        String imageUrl,
        LocalDateTime registeredAt,
        EventDto linkedEvent
) {
    public static EventNoticeDto of(EventNotice notice) {
        return new EventNoticeDto(
                notice.getId(),
                notice.getType(),
                notice.getTitle(),
                notice.getContent(),
                notice.getImageUrl(),
                notice.getRegisteredAt(),
                EventDto.of(notice.getLinkedEvent())
        );
    }
}
