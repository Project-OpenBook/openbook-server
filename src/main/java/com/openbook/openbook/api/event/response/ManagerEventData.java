package com.openbook.openbook.api.event.response;

import static com.openbook.openbook.util.Formatter.getFormattingDate;
import com.openbook.openbook.service.event.dto.EventDto;
import com.openbook.openbook.service.event.dto.EventTagDto;
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
        String status,
        LocalDateTime registerDate
) {
    public static ManagerEventData of(EventDto event) {
        return new ManagerEventData(
                event.id(),
                event.name(),
                event.mainImageUrl(),
                event.location(),
                event.description(),
                getFormattingDate(event.openDate().atStartOfDay()),
                getFormattingDate(event.closeDate().atStartOfDay()),
                getFormattingDate(event.b_RecruitmentStartDate().atStartOfDay()),
                getFormattingDate(event.b_RecruitmentEndDate().atStartOfDay()),
                event.tags().stream().map(EventTagDto::name).toList(),
                event.status(),
                event.registeredAt()
        );
    }
}
