package com.openbook.openbook.eventmanager.dto;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public record BoothMangeData(
        Long id,
        String name,
        List<BoothLocationData> boothLocationData,
        String registrationDate,
        String description,
        @Enumerated(EnumType.STRING)
        BoothStatus status
) {
    public static BoothMangeData of(Booth booth, List<BoothLocationData> boothLocationData) {
        return new BoothMangeData(
                booth.getId(),
                booth.getName(),
                boothLocationData,
                booth.getDescription(),
                getFormattingDate(booth.getRegisteredAt()),
                booth.getStatus()
        );
    }
}
