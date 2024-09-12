package com.openbook.openbook.api.event.response;

import static com.openbook.openbook.util.Formatter.getFormattingDate;
import static com.openbook.openbook.util.JsonService.convertJsonToList;
import com.openbook.openbook.service.event.dto.EventDto;
import com.openbook.openbook.service.event.dto.EventTagDto;
import com.openbook.openbook.api.user.response.UserPublicResponse;
import java.util.List;

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
        UserPublicResponse eventManager,
        int boothCount
) {
    public static EventDetail of(EventDto event, int boothCount) {
        return new EventDetail(
                event.id(),
                event.name(),
                event.mainImageUrl(),
                event.location(),
                event.description(),
                getFormattingDate(event.openDate().atStartOfDay()),
                getFormattingDate(event.closeDate().atStartOfDay()),
                event.tags().stream().map(EventTagDto::name).toList(),
                convertJsonToList(event.layoutImageUrls()),
                UserPublicResponse.of(event.manager()),
                boothCount
        );
    }
}
