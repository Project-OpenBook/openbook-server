package com.openbook.openbook.user.controller.response;

import com.openbook.openbook.user.dto.UserDto;

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
