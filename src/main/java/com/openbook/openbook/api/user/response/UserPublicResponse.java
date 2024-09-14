package com.openbook.openbook.api.user.response;

import com.openbook.openbook.service.user.dto.UserDto;

public record UserPublicResponse(
        Long id,
        String nickname,
        String role
) {
    public static UserPublicResponse of(UserDto user) {
        return new UserPublicResponse(
                user.id(),
                user.nickname(),
                user.role()
        );
    }
}
