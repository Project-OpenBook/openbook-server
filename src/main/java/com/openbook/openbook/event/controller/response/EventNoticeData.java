package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.entity.EventNotice;
import com.openbook.openbook.event.entity.dto.EventNoticeType;
import java.time.LocalDateTime;

public record EventNoticeData(
        String title,
        String content,
        String imageUrl,
        EventNoticeType type,
        LocalDateTime registeredAt
) {
    public static EventNoticeData of(EventNotice notice) {
        return new EventNoticeData(
                notice.getTitle(),
                notice.getContent(),
                notice.getImageUrl(),
                EventNoticeType.valueOf(notice.getType()),
                notice.getRegisteredAt()
        );
    }
}
