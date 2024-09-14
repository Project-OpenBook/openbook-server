package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.domain.event.EventLayout;
import com.openbook.openbook.domain.event.dto.EventLayoutType;
import java.util.List;


public record EventLayoutDto(
        long id,
        String imageUrl,
        EventLayoutType type,
        List<BoothAreaDto> areas
) {
    public static EventLayoutDto of(EventLayout layout) {
        return new EventLayoutDto(
                layout.getId(),
                layout.getImageUrl(),
                layout.getType(),
                layout.getAreas().stream().map(BoothAreaDto::of).toList()
        );
    }
}
