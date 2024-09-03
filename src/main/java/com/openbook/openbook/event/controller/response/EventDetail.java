package com.openbook.openbook.event.controller.response;

import static com.openbook.openbook.global.util.Formatter.getFormattingDate;
import static com.openbook.openbook.global.util.JsonService.convertJsonToList;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.user.dto.UserPublicData;
import java.util.List;
import java.util.Objects;

public record EventDetail(
        Long id,
        String name,
        String mainImageUrl,
        String location,
        String description,
        String openDate,
        String closeDate,
        List<String> tags,
        List<String> layoutImageUrls,
        UserPublicData event_manager,
        int boothCount
) {
    public static EventDetail of(Event event, List<EventTag> tags, int boothCount) {
        return new EventDetail(
                event.getId(),
                event.getName(),
                event.getMainImageUrl(),
                event.getLocation(),
                event.getDescription(),
                getFormattingDate(event.getOpenDate().atStartOfDay()),
                getFormattingDate(event.getCloseDate().atStartOfDay()),
                tags.stream().map(EventTag::getName).toList(),
                convertJsonToList(event.getLayout().getImageUrl()),
                UserPublicData.of(event.getManager()),
                boothCount
        );
    }
}
