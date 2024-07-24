package com.openbook.openbook.basicuser.dto.response;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;

import java.util.List;

import static com.openbook.openbook.global.util.Formatter.getFormattingTime;

public record BoothDetail(
        Long id,
        String name,
        Long eventId,
        String eventName,
        String openTime,
        String closeTime,
        List<BoothAreaData> location,
        String description,
        String mainImageUrl,
        boolean isBoothManager
) {
    public static BoothDetail of(Booth booth, List<BoothAreaData> boothAreaData, boolean isBoothManager){
        return new BoothDetail(
                booth.getId(),
                booth.getName(),
                booth.getLinkedEvent().getId(),
                booth.getLinkedEvent().getName(),
                getFormattingTime(booth.getOpenTime()),
                getFormattingTime(booth.getCloseTime()),
                boothAreaData,
                booth.getDescription(),
                booth.getMainImageUrl(),
                isBoothManager
        );
    }
}
