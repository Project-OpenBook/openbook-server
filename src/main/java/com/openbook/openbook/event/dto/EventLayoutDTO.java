package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.dto.EventLayoutType;
import lombok.Builder;

@Builder
public record EventLayoutDTO(
        String imageUrl,
        EventLayoutType type
) {
}
