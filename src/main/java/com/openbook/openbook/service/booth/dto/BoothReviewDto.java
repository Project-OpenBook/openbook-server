package com.openbook.openbook.service.booth.dto;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothReview;
import com.openbook.openbook.service.user.dto.UserDto;

import java.time.LocalDateTime;

public record BoothReviewDto(
        UserDto reviewer,
        Booth linkedBooth,
        long id,
        float star,
        String content,
        String image,
        LocalDateTime registerAt
) {
    public static BoothReviewDto of(BoothReview review){
        return new BoothReviewDto(
                UserDto.of(review.getReviewer()),
                review.getLinkedBooth(),
                review.getId(),
                review.getStar(),
                review.getContent(),
                review.getImageUrl(),
                review.getRegisteredAt()
        );
    }
}
