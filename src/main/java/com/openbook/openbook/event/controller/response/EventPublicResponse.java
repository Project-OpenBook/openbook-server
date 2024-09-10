package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.dto.EventDto;
import com.openbook.openbook.user.controller.response.UserPublicResponse;

public record EventPublicResponse(
        long id,
        String name,
        UserPublicResponse manager
) {
    public static EventPublicResponse of(EventDto event) {
        return new EventPublicResponse(
                event.id(),
                event.name(),
                UserPublicResponse.of(event.manager())
        );
    }
}
