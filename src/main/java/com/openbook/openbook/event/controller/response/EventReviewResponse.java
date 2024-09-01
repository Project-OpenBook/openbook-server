package com.openbook.openbook.event.controller.response;

import com.openbook.openbook.event.entity.EventReview;
import com.openbook.openbook.event.entity.EventReviewImage;
import com.openbook.openbook.user.dto.UserPublicData;
import java.time.LocalDateTime;
import java.util.List;

public record EventReviewResponse(
        UserPublicData reviewer,
        long id,
        float star,
        String content,
        List<EventReviewImageData> images,
        LocalDateTime registerDate
) {
    public static EventReviewResponse of(EventReview eventReview, List<EventReviewImage> images) {
        return new EventReviewResponse(
                UserPublicData.of(eventReview.getReviewer()),
                eventReview.getId(),
                eventReview.getStar(),
                eventReview.getContent(),
                images.stream().map(EventReviewImageData::of).toList(),
                eventReview.getRegisteredAt()
        );
    }
}
