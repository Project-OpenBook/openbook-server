package com.openbook.openbook.event.dto;

import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.user.entity.User;

public record EventReviewDto(
        User reviewer,
        Event linkedEvent,
        float star,
        String content
) {
}
