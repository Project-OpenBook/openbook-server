package com.openbook.openbook.api.event.response;

import com.openbook.openbook.service.event.dto.EventReviewDto;
import com.openbook.openbook.api.user.response.UserPublicResponse;
import java.time.LocalDateTime;
import java.util.List;

public record EventReviewResponse(
        UserPublicResponse reviewer,
        long id,
        float star,
        String content,
        List<EventReviewImageResponse> images,
        LocalDateTime registerDate
) {
    public static EventReviewResponse of(EventReviewDto eventReview) {
        return new EventReviewResponse(
                UserPublicResponse.of(eventReview.reviewer()),
                eventReview.id(),
                eventReview.star(),
                eventReview.content(),
                eventReview.images().stream().map(EventReviewImageResponse::of).toList(),
                eventReview.registeredAt()
        );
    }
}
