package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.service.booth.dto.BoothDto;
import com.openbook.openbook.api.user.response.UserPublicResponse;

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
