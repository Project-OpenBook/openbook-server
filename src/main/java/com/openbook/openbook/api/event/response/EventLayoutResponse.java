package com.openbook.openbook.api.event.response;

import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.service.event.dto.EventLayoutDto;
import com.openbook.openbook.domain.event.dto.EventLayoutType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EventLayoutResponse(
        long id,
        String imageUrl,
        EventLayoutType type,
        Map<String, List<BoothAreaDto>> areas
) {
        public static EventLayoutResponse of(final EventLayoutDto layout) {
                return new EventLayoutResponse(
                        layout.id(),
                        layout.imageUrl(),
                        layout.type(),
                        layout.areas().stream().collect(
                                Collectors.groupingBy(
                                        BoothAreaDto::classification
                                )
                        )
                );
        }
}
