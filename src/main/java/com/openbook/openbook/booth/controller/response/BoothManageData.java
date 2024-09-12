package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.service.dto.BoothAreaDto;
import com.openbook.openbook.booth.service.dto.BoothDto;
import com.openbook.openbook.booth.service.dto.BoothTagDto;

import java.util.List;

public record BoothManageData(
        Long id,
        String name,
        String mainImageUrl,
        List<BoothAreaDto> boothLocationData,
        String registrationDate,
        String description,
        List<String> tags,
        String status
) {
    public static BoothManageData of(BoothDto booth) {
        return new BoothManageData(
                booth.id(),
                booth.name(),
                booth.mainImageUrl(),
                booth.locations(),
                getFormattingDate(booth.registeredAt()),
                booth.description(),
                booth.tags().stream().map(BoothTagDto::name).toList(),
                booth.status()
        );
    }
}
