package com.openbook.openbook.booth.controller.response;

import com.openbook.openbook.booth.service.dto.BoothDto;
import com.openbook.openbook.user.controller.response.UserPublicResponse;

public record BoothPublicResponse(
        long id,
        String name,
        UserPublicResponse manager
) {
    public static BoothPublicResponse of(BoothDto booth) {
        return new BoothPublicResponse(
                booth.id(),
                booth.name(),
                UserPublicResponse.of(booth.manager())
        );
    }
}
