package com.openbook.openbook.basicuser.dto.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import static com.openbook.openbook.global.util.JsonService.convertJsonToList;

import com.openbook.openbook.event.entity.Event;
import java.util.List;

public record EventDetail(
        Long id,
        String name,
        String mainImageUrl,
        String location,
        String description,
        String openDate,
        String closeDate,
        List<String> layoutImageUrls,
        int boothCount,
        boolean isUserManager
) {
    public static EventDetail of(Event event, int boothCount, boolean isUserManager) {
        return new EventDetail(
                event.getId(),
                event.getName(),
                event.getMainImageUrl(),
                event.getLocation(),
                event.getDescription(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                convertJsonToList(event.getLayout().getImageUrl()),
                boothCount,
                isUserManager
        );
    }
}
