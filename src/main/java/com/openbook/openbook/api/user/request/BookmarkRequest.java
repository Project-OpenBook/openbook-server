package com.openbook.openbook.api.user.request;

import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(
        @NotNull String type,
        @NotNull long resourceId,
        Boolean alarmSet
) {
}
