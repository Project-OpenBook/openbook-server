package com.openbook.openbook.eventmanager.dto;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public record BoothManageData(
        Long id,
        String name,
        List<BoothAreaData> boothLocationData,
        String registrationDate,
        String description,
        @Enumerated(EnumType.STRING)
        BoothStatus status
) {
    public static BoothManageData of(Booth booth, List<BoothAreaData> boothLocationData) {
        return new BoothManageData(
                booth.getId(),
                booth.getName(),
                boothLocationData,
                getFormattingDate(booth.getRegisteredAt()),
                booth.getDescription(),
                booth.getStatus()
        );
    }
}
