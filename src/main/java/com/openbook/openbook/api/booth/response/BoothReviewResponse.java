package com.openbook.openbook.api.booth.response;

import com.openbook.openbook.api.user.response.UserPublicResponse;
import com.openbook.openbook.service.booth.dto.BoothReviewDto;

import java.time.LocalDateTime;

public record BoothReviewResponse(
        UserPublicResponse reviewer,
        long id,
        float star,
        String content,
        String image,
        LocalDateTime registerDate
) {
    public static BoothReviewResponse of(BoothReviewDto boothReviewDto){
        return new BoothReviewResponse(
                UserPublicResponse.of(boothReviewDto.reviewer()),
                boothReviewDto.id(),
                boothReviewDto.star(),
                boothReviewDto.content(),
                boothReviewDto.image(),
                boothReviewDto.registerAt()
        );
    }
}
