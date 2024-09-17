package com.openbook.openbook.api.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.openbook.openbook.api.booth.response.BoothBasicData;
import com.openbook.openbook.api.event.response.UserEventData;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import com.openbook.openbook.service.user.dto.BookmarkDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookmarkResponse(
        long id,
        String bookmarkType,
        UserEventData event,
        BoothBasicData booth
) {
    public static BookmarkResponse of(BookmarkDto bookmark) {
        return new BookmarkResponse(
                bookmark.id(),
                bookmark.type().name(),
                (bookmark.type()== BookmarkType.EVENT) ? UserEventData.of(bookmark.event()) : null,
                (bookmark.type()== BookmarkType.BOOTH) ? BoothBasicData.of(bookmark.booth()) : null
        );
    }
}
