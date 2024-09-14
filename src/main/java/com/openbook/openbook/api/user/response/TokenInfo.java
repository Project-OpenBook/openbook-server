package com.openbook.openbook.api.user.response;

import lombok.Builder;

@Builder
public record TokenInfo(
        Long id,
        String nickname,
        String role,
        Long expires_in
) {
}
