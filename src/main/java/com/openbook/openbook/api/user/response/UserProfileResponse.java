package com.openbook.openbook.api.user.response;

import com.openbook.openbook.service.user.dto.UserProfileDto;

public record UserProfileResponse(
        Long id,
        String name,
        String nickname,
        String email
) {
    public static UserProfileResponse of(UserProfileDto dto){
        return new UserProfileResponse(
                dto.id(),
                dto.name(),
                dto.nickname(),
                dto.email()
        );
    }
}
