package com.openbook.openbook.api.event.response;

import com.openbook.openbook.service.event.dto.EventDto;
import com.openbook.openbook.api.user.response.UserPublicResponse;

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
