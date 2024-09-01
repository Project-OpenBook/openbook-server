package com.openbook.openbook.user.dto;

import com.openbook.openbook.user.entity.User;

public record UserPublicData(
        Long id,
        String nickname,
        String role
) {
    public static UserPublicData of(User user) {
        return new UserPublicData(
                user.getId(),
                user.getNickname(),
                user.getRole().name()
        );
    }
}
