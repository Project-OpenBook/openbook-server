package com.openbook.openbook.api.booth.response;

import static com.openbook.openbook.util.Formatter.getFormattingDate;

import com.openbook.openbook.api.event.response.EventPublicResponse;
import com.openbook.openbook.service.booth.dto.BoothAreaDto;
import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.service.booth.dto.BoothTagDto;

import java.util.List;

public record BoothManageData(
        Long id,
        String name,
        String mainImageUrl,
        List<BoothAreaDto> boothLocationData,
        String registrationDate,
        String closeDate,
        String description,
        List<String> tags,
        String status,
        EventPublicResponse event
) {
    public static BoothManageData of(BoothDto booth) {
        return new BoothManageData(
                booth.id(),
                booth.name(),
                booth.mainImageUrl(),
                booth.locations(),
                getFormattingDate(booth.registeredAt()),
                getFormattingDate(booth.closeTime()),
                booth.description(),
                booth.tags().stream().map(BoothTagDto::name).toList(),
                booth.status(),
                EventPublicResponse.of(booth.linkedEvent())
        );
    }
}
