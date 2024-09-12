package com.openbook.openbook.service.user.dto;

import com.openbook.openbook.domain.user.User;

public record UserDto(
        Long id,
        String name,
        String nickname,
        String role
) {
    public static UserDto of(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getRole().name()
        );
    }
}
