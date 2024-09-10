package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.EventTag;

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
