package com.openbook.openbook.basicuser.dto.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.event.entity.Event;

public record EventBasicData(
        Long id,
        String name,
        String mainImageUrl,
        String openDate,
        String closeDate,
        String recruitStartDate,
        String recruitEndDate
) {
    public static EventBasicData of(Event event) {
        return new EventBasicData(
                event.getId(),
                event.getName(),
                event.getMainImageUrl(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentStartDate().atStartOfDay()),
                getFormattingDate(event.getBoothRecruitmentEndDate().atStartOfDay())
        );
    }
}
