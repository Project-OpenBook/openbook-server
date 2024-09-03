package com.openbook.openbook.event.service.core;

import com.openbook.openbook.event.dto.EventReviewDto;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventReview;
import com.openbook.openbook.event.entity.EventReviewImage;
import com.openbook.openbook.event.repository.EventReviewImageRepository;
import com.openbook.openbook.event.repository.EventReviewRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventReviewService {

    private final EventReviewRepository eventReviewRepository;
    private final EventReviewImageRepository eventReviewImageRepository;
    private final S3Service s3Service;

    public EventReview getEventReviewOrException(long eventReviewId) {
        return eventReviewRepository.findById(eventReviewId).orElseThrow(()->
                new OpenBookException(ErrorCode.REVIEW_NOT_FOUND)
        );
    }

    public EventReview createEventReview(EventReviewDto dto){
        return eventReviewRepository.save(
                EventReview.builder()
                        .reviewer(dto.reviewer())
                        .linkedEvent(dto.linkedEvent())
                        .star(dto.star())
                        .content(dto.content())
                        .build()
        );

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

    public Slice<EventReview> getReviewsOf(Event linkedEvent, Pageable pageable){
        return eventReviewRepository.findByLinkedEventId(linkedEvent.getId(), pageable);
    }

    public List<EventReviewImage> getReviewImagesOf(EventReview linkedReview) {
        return eventReviewImageRepository.findAllByLinkedReviewId(linkedReview.getId());
    }

}
