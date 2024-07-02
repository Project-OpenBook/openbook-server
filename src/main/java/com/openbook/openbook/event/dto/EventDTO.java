package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.user.entity.User;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record EventDTO(
        User manager,
        EventLayout layout,
        String location,
        String name,
        String mainImageUrl,
        String description,
        LocalDate openDate,
        LocalDate closeDate,
        LocalDate b_RecruitmentStartDate,
        LocalDate b_RecruitmentEndDate
) {
}
