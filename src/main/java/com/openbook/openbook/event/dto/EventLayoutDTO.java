package com.openbook.openbook.event.dto;

import lombok.Builder;

@Builder
public record EventLayoutDTO(
        String imageUrl,
        EventLayoutType type
) {
}
