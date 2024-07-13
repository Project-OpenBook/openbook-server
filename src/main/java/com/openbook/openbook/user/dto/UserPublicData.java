package com.openbook.openbook.user.dto;

import com.openbook.openbook.user.entity.User;

public record UserPublicData(
        Long id,
        String nickname
) {
    public static UserPublicData of(User user) {
        return new UserPublicData(
                user.getId(),
                user.getNickname()
        );
    }
}
