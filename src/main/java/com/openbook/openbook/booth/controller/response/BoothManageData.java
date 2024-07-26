package com.openbook.openbook.booth.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public record BoothManageData(
        Long id,
        String name,
        List<BoothAreaData> boothLocationData,
        String registrationDate,
        String description,
        List<String> tags,
        @Enumerated(EnumType.STRING)
        BoothStatus status
) {
    public static BoothManageData of(Booth booth, List<BoothAreaData> boothAreas, List<BoothTag> tags) {
        return new BoothManageData(
                booth.getId(),
                booth.getName(),
                boothAreas,
                getFormattingDate(booth.getRegisteredAt()),
                booth.getDescription(),
                tags.stream().map(BoothTag::getName).toList(),
                booth.getStatus()
        );
    }
}
