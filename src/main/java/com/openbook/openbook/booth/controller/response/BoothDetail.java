package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;

import com.openbook.openbook.user.dto.UserPublicData;
import java.util.List;

import static com.openbook.openbook.global.util.Formatter.getFormattingTime;

public record BoothDetail(
        Long id,
        String name,
        String description,
        String mainImageUrl,
        String openTime,
        String closeTime,
        List<BoothAreaData> location,
        List<String> tags,
        UserPublicData boothManager,
        Long eventId,
        String eventName
) {

    public static BoothDetail of(Booth booth, List<BoothAreaData> boothAreas, List<BoothTag> tags){
        return new BoothDetail(
                booth.getId(),
                booth.getName(),
                booth.getDescription(),
                booth.getMainImageUrl(),
                getFormattingTime(booth.getOpenTime()),
                getFormattingTime(booth.getCloseTime()),
                boothAreas,
                tags.stream().map(BoothTag::getName).toList(),
                UserPublicData.of(booth.getManager()),
                booth.getLinkedEvent().getId(),
                booth.getLinkedEvent().getName()
        );
    }
}
