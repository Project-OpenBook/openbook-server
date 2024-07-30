package com.openbook.openbook.event.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.entity.EventTag;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

public record ManagerEventData(
        Long id,
        String name,
        String mainImageUrl,
        String location,
        String description,
        String openDate,
        String closeDate,
        String recruitStartDate,
        String recruitEndDate,
        List<String> tags,
        @Enumerated(EnumType.STRING)
        EventStatus status,
        LocalDateTime registerDate
) {
    public static ManagerEventData of(Event event, List<EventTag> tags) {
        return new ManagerEventData(
                event.getId(),
                event.getName(),
                event.getMainImageUrl(),
                event.getLocation(),
                event.getDescription(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentStartDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentEndDate().atStartOfDay()),
                tags.stream().map(EventTag::getName).toList(),
                event.getStatus(),
                event.getRegisteredAt()
        );
    }
}
