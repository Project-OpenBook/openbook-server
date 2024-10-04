package com.openbook.openbook.service.booth.dto;


import lombok.Builder;

@Builder
public record BoothReviewUpdateData(
        Float star,
        String content,
        String image
) {
}
