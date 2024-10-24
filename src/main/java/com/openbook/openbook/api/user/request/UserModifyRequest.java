package com.openbook.openbook.api.user.request;

import com.openbook.openbook.service.user.dto.UserUpdateDto;

public record UserModifyRequest(
        String name,
        String nickname,
        String email
) {
    public static UserModifyRequest of(UserUpdateDto dto){
        return new UserModifyRequest(
                dto.name(),
                dto.nickname(),
                dto.email()
        );
    }
}
