package com.openbook.openbook.api.event.response;

import static com.openbook.openbook.util.Formatter.getFormattingDate;
import com.openbook.openbook.service.event.dto.EventDto;
import com.openbook.openbook.service.event.dto.EventTagDto;
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
