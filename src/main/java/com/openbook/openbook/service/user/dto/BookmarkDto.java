package com.openbook.openbook.service.user.dto;

import com.openbook.openbook.domain.user.Bookmark;
import com.openbook.openbook.domain.user.dto.BookmarkType;
import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.service.event.dto.EventDto;

public record BookmarkDto(
        long id,
        BookmarkType type,
        EventDto event,
        BoothDto booth
) {
    public static BookmarkDto of(Bookmark bookmark, EventDto event, BoothDto booth) {
        return new BookmarkDto(
                bookmark.getId(),
                bookmark.getBookmarkType(),
                event,
                booth
        );
    }
}
