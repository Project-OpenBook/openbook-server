package com.openbook.openbook.service.event;

import com.openbook.openbook.api.event.request.EventReviewRegisterRequest;
import com.openbook.openbook.service.event.dto.EventReviewDto;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.event.EventReview;
import com.openbook.openbook.domain.event.EventReviewImage;
import com.openbook.openbook.domain.event.dto.EventStatus;
import com.openbook.openbook.repository.event.EventReviewImageRepository;
import com.openbook.openbook.repository.event.EventReviewRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.user.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventReviewService {

    private final EventReviewRepository eventReviewRepository;
    private final EventReviewImageRepository eventReviewImageRepository;
    private final S3Service s3Service;

    private final UserService userService;
    private final EventService eventService;

    public EventReview getEventReviewOrException(long eventReviewId) {
        return eventReviewRepository.findById(eventReviewId).orElseThrow(()->
                new OpenBookException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public Slice<EventReviewDto> getEventReviews(final long eventId, final Pageable pageable) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.EVENT_NOT_APPROVED);
        }
        return eventReviewRepository.findByLinkedEventId(event.getId(), pageable).map(EventReviewDto::of);
    }

    @Transactional(readOnly = true)
    public EventReviewDto getEventReview(final long reviewId) {
        return EventReviewDto.of(getEventReviewOrException(reviewId));
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
        EventReview review = eventReviewRepository.save(EventReview.builder()
                .reviewer(user)
                .linkedEvent(event)
                .star(request.star())
                .content(request.content())
                .build()
        );
        if(request.images() != null) {
            for(int i=0;i<request.images().size();i++) {
                createEventReviewImage(review, request.images().get(i), i);
            }
        }
    }

    @Transactional
    public void deleteReview(long userId, long reviewId) {
        EventReview review = getEventReviewOrException(reviewId);
        if(review.getReviewer().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        eventReviewRepository.deleteById(reviewId);
    }


    public void createEventReviewImage(EventReview linkedReview, MultipartFile image, int order){
        eventReviewImageRepository.save(
                EventReviewImage.builder()
                        .linkedReview(linkedReview)
                        .imageUrl(s3Service.uploadFileAndGetUrl(image))
                        .imageOrder(order)
                        .build()
        );
    }

}
