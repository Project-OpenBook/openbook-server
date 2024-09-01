package com.openbook.openbook.event.service.common;

import com.openbook.openbook.event.controller.request.EventReviewRegisterRequest;
import com.openbook.openbook.event.controller.response.EventReviewResponse;
import com.openbook.openbook.event.dto.EventReviewDto;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventReview;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.service.core.EventReviewService;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonEventReviewService {

    private final UserService userService;
    private final EventService eventService;
    private final EventReviewService eventReviewService;

    @Transactional(readOnly = true)
    public Slice<EventReviewResponse> getEventReviews(final long eventId, final Pageable pageable) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.EVENT_NOT_APPROVED);
        }
        return eventReviewService.getReviewsOf(event, pageable).map(eventReview ->
                EventReviewResponse.of(eventReview, eventReviewService.getReviewImagesOf(eventReview))
        );
    }

    @Transactional
    public void registerEventReview(Long loginUser, EventReviewRegisterRequest request) {
        User user = userService.getUserOrException(loginUser);
        Event event = eventService.getEventOrException(request.event_id());

        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.EVENT_NOT_APPROVED);
        }
        if(event.getOpenDate().isAfter(LocalDate.now())) {
            throw new OpenBookException(ErrorCode.CANNOT_REVIEW_PERIOD);
        }

        EventReviewDto dto = new EventReviewDto(user, event, request.star(), request.content());
        EventReview review = eventReviewService.createEventReview(dto);
        if(request.images() != null) {
            for(int i=0;i<request.images().size();i++) {
                eventReviewService.createEventReviewImage(review, request.images().get(i), i);
            }
        }
    }


}
