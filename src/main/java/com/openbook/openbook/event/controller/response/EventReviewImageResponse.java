package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.dto.EventReviewImageDto;

public record EventReviewImageResponse(
        long id,
        String url,
        int seq
) {
    public static EventReviewImageResponse of(EventReviewImageDto image) {
        return new EventReviewImageResponse(
                image.id(),
                image.imageUrl(),
                image.seq()
        );
    }
}
