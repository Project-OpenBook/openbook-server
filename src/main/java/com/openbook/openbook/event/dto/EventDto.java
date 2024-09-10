package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.user.dto.UserDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        UserDto manager,
        //EventLayout layout,
        long id,
        String name,
        String description,
        String location,
        String mainImageUrl,
        LocalDate openDate,
        LocalDate closeDate,
        LocalDate b_RecruitmentStartDate,
        LocalDate b_RecruitmentEndDate,
        List<EventTagDto> tags,
        String layoutImageUrls,
        String status,
        LocalDateTime registeredAt
) {
    public static EventDto of(Event event) {
        return new EventDto(
                UserDto.of(event.getManager()),
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getDescription(),
                event.getLocation(),
                event.getOpenDate(),
                event.getCloseDate(),
                event.getBoothRecruitmentStartDate(),
                event.getBoothRecruitmentEndDate(),
                event.getEventTags().stream().map(EventTagDto::of).toList(),
                event.getLayout().getImageUrl(),
                event.getStatus().name(),
                event.getRegisteredAt()
        );
    }
}
