package com.openbook.openbook.basicuser.dto.response;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.event.entity.Event;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

public record BoothBasicData(
        Long id,
        String name,
        String openDate,
        String closeDate,
        String mainImageUrl
) {
    public static BoothBasicData of(Booth booth, Event event){
        return new BoothBasicData(
                booth.getId(),
                booth.getName(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                booth.getMainImageUrl()
        );
    }
}
