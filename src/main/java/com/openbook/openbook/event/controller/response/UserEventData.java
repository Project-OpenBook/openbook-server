package com.openbook.openbook.event.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import com.openbook.openbook.event.dto.EventDto;
import com.openbook.openbook.event.dto.EventTagDto;
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
    public static UserEventData of(EventDto event) {
        return new UserEventData(
                event.id(),
                event.name(),
                event.mainImageUrl(),
                getFormattingDate(event.openDate().atStartOfDay()),
                getFormattingDate(event.closeDate().atStartOfDay()),
                getFormattingDate(event.b_RecruitmentStartDate().atStartOfDay()),
                getFormattingDate(event.b_RecruitmentEndDate().atStartOfDay()),
                event.tags().stream().map(EventTagDto::name).toList()
        );
    }
}
