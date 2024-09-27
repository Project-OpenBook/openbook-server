package com.openbook.openbook.service.event;


import com.openbook.openbook.domain.event.EventReview;
import com.openbook.openbook.domain.event.EventReviewImage;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.repository.event.EventReviewImageRepository;
import com.openbook.openbook.util.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EventReviewImageService {

    private final EventReviewImageRepository eventReviewImageRepository;
    private final S3Service s3Service;

    public EventReviewImage getEventReviewImageOrException(long eventReviewImageId) {
        return eventReviewImageRepository.findById(eventReviewImageId).orElseThrow(()->
                new OpenBookException(ErrorCode.IMAGE_NOT_FOUND)
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

    public void modifyReviewImage(List<MultipartFile> add, List<Long> delete, EventReview review) {
        int addSize = (add!=null)?add.size():0, deleteSize = (delete!=null)?delete.size():0;
        if(review.getReviewImages().size() - deleteSize + addSize > 5) {
            throw new OpenBookException(ErrorCode.EXCEED_MAXIMUM_IMAGE);
        }
        for(int i = 0; i < addSize; i++) {
            createEventReviewImage(review, add.get(i), i);
        }
        for(int i = 0; i < deleteSize; i++) {
            EventReviewImage image = getEventReviewImageOrException(delete.get(i));
            if(image.getLinkedReview()!=review) {
                throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
            }
            eventReviewImageRepository.delete(image);
        }
    }
}
