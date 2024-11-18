package com.openbook.openbook.service.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateDto(
        String name,
        String nickname,
        String email
) {
}
