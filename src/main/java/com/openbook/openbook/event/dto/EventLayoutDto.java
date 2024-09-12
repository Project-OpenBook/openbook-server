package com.openbook.openbook.event.dto;

import com.openbook.openbook.booth.service.dto.BoothAreaDto;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.dto.EventLayoutType;
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
