package com.openbook.openbook.api.user.request;

import com.openbook.openbook.domain.user.dto.BookmarkType;
import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(
        @NotNull BookmarkType type,
        @NotNull long resourceId,
        Boolean alarmSet
) {
}
