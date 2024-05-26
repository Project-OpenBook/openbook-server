package com.openbook.openbook.basicuser.dto;

import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record LayoutAreaStatusData(
        Long id,
        @Enumerated(EnumType.STRING)
        EventLayoutAreaStatus status,
        String number
) {
}
