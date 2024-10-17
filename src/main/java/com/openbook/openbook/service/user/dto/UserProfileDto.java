package com.openbook.openbook.service.user.dto;

import com.openbook.openbook.domain.user.User;

public record UserProfileDto(
        Long id,
        String name,
        String nickname,
        String email
) {
    public static UserProfileDto of(User user){
        return new UserProfileDto(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail()
        );
    }
}
