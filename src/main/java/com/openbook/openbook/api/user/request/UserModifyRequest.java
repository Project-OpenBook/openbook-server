package com.openbook.openbook.api.user.request;

public record UserModifyRequest(
        String name,
        String nickname,
        String email
) {
}
