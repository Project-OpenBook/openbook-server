package com.openbook.openbook.basicuser.dto.response;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;

import java.time.LocalDateTime;
import java.util.List;

import static com.openbook.openbook.global.util.Formatter.getFormattingTime;

public record BoothDetail(
        Long id,
        String name,
        String openTime,
        String closeTime,
        List<BoothAreaData> location,
        String description,
        String mainImageUrl
) {
    public static BoothDetail of(Booth booth, List<BoothAreaData> boothAreaData){
        return new BoothDetail(
                booth.getId(),
                booth.getName(),
                getFormattingTime(booth.getOpenTime()),
                getFormattingTime(booth.getCloseTime()),
                boothAreaData,
                booth.getDescription(),
                booth.getMainImageUrl()
        );
    }
}
