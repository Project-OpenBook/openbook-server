package com.openbook.openbook.event.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.entity.EventTag;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;

public record AdminEventData(
        Long id,
        String name,
        String location,
        String registrationDate,
        String description,
        List<String> tags,
        @Enumerated(EnumType.STRING)
        EventStatus status
) {
    public static AdminEventData of(Event event, List<EventTag> tags) {
        return new AdminEventData(
                event.getId(),
                event.getName(),
                event.getLocation(),
                getFormattingDate(event.getRegisteredAt()),
                event.getDescription(),
                tags.stream().map(EventTag::getName).toList(),
                event.getStatus()
        );
    }
}
