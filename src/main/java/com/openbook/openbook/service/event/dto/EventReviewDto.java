package com.openbook.openbook.service.event.dto;

import com.openbook.openbook.domain.event.EventReview;
import com.openbook.openbook.service.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.List;

public record EventReviewDto(
        long id,
        float star,
        String content,
        List<EventReviewImageDto> images,
        LocalDateTime registeredAt,
        UserDto reviewer,
        EventDto linkedEvent
) {
    public static EventReviewDto of(EventReview review) {
        return new EventReviewDto(
                review.getId(),
                review.getStar(),
                review.getContent(),
                review.getReviewImages().stream().map(EventReviewImageDto::of).toList(),
                review.getRegisteredAt(),
                UserDto.of(review.getReviewer()),
                EventDto.of(review.getLinkedEvent())
        );
    }
}
