package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.entity.EventReviewImage;

public record EventReviewImageData(
        long id,
        String url,
        int seq
) {
    public static EventReviewImageData of(EventReviewImage image) {
        return new EventReviewImageData(
                image.getId(),
                image.getImageUrl(),
                image.getImageOrder()
        );
    }
}
