package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.domain.event.EventReviewImage;

public record EventReviewImageDto(
        long id,
        String imageUrl,
        int seq
) {
    public static EventReviewImageDto of(EventReviewImage image) {
        return new EventReviewImageDto(
                image.getId(),
                image.getImageUrl(),
                image.getImageOrder()
        );
    }
}
