package com.openbook.openbook.administrator.dto;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.dto.EventStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record AdminEventData(
        Long id,
        String name,
        String location,
        String registrationDate,
        String description,
        @Enumerated(EnumType.STRING)
        EventStatus status
) {
    public static AdminEventData of(Event event) {
        return new AdminEventData(
                event.getId(),
                event.getName(),
                event.getLocation(),
                getFormattingDate(event.getRegisteredAt()),
                event.getDescription(),
                event.getStatus()
        );
    }
}
