package com.openbook.openbook.user.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        String email,
        String password,
        String name,
        String nickname
) {
}
