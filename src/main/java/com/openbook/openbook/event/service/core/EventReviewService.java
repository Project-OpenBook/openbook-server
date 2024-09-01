package com.openbook.openbook.event.service.core;

import com.openbook.openbook.event.dto.EventReviewDto;
import com.openbook.openbook.event.entity.EventReview;
import com.openbook.openbook.event.entity.EventReviewImage;
import com.openbook.openbook.event.repository.EventReviewImageRepository;
import com.openbook.openbook.event.repository.EventReviewRepository;
import com.openbook.openbook.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventReviewService {

    private final EventReviewRepository eventReviewRepository;
    private final EventReviewImageRepository eventReviewImageRepository;
    private final S3Service s3Service;

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

}
