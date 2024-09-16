package com.openbook.openbook.domain.user.dto;

import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;

public enum BookmarkType {
    EVENT,
    BOOTH;
    public static BookmarkType fromString(String request) {
        return switch (request) {
            case "EVENT" -> EVENT;
            case "BOOTH" -> BOOTH;
            default -> throw new OpenBookException(ErrorCode.INVALID_BOOKMARK_TYPE);
        };
    }
}
