package com.openbook.openbook.basicuser.dto.response;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.event.entity.Event;
import java.util.List;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;

public record BoothBasicData(
        Long id,
        String name,
        String eventName,
        String openDate,
        String closeDate,
        String mainImageUrl,
        List<String> tags
) {
    public static BoothBasicData of(Booth booth, Event event, List<BoothTag> tags){
        return new BoothBasicData(
                booth.getId(),
                booth.getName(),
                event.getName(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                booth.getMainImageUrl(),
                tags.stream().map(BoothTag::getName).toList()
        );
    }
}
