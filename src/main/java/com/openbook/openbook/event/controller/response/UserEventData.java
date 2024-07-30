package com.openbook.openbook.event.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventTag;
import java.util.List;

public record UserEventData(
        Long id,
        String name,
        String mainImageUrl,
        String openDate,
        String closeDate,
        String recruitStartDate,
        String recruitEndDate,
        List<String> tags
) {
    public static UserEventData of(Event event, List<EventTag> tags) {
        return new UserEventData(
                event.getId(),
                event.getName(),
                event.getMainImageUrl(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentStartDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentEndDate().atStartOfDay()),
                tags.stream().map(EventTag::getName).toList()
        );
    }
}
