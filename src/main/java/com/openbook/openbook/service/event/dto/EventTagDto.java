package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.domain.event.EventTag;

public record EventTagDto(
        long id,
        String name
) {
    public static EventTagDto of(EventTag eventTag) {
        return new EventTagDto(
                eventTag.getId(),
                eventTag.getName()
        );
    }
}
