package com.openbook.openbook.service.booth;

import com.openbook.openbook.api.booth.request.BoothReviewRegisterRequest;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.BoothReview;
import com.openbook.openbook.repository.booth.BoothReviewRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.booth.dto.BoothReviewDto;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.user.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothReviewService {
    private final BoothReviewRepository boothReviewRepository;
    private final S3Service s3Service;

    private final UserService userService;
    private final BoothService boothService;

    public BoothReview getBoothReviewOrException(long reviewId){
        return boothReviewRepository.findById(reviewId).orElseThrow(
                () -> new OpenBookException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    @Transactional
    public void registerBoothReview(Long userId, BoothReviewRegisterRequest request){
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(request.booth_id());
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        if(booth.getLinkedEvent().getOpenDate().isAfter(LocalDate.now())){
            throw new OpenBookException(ErrorCode.CANNOT_REVIEW_PERIOD);
        }
        String image = (request.image()!=null)?s3Service.uploadFileAndGetUrl(request.image()):null;
        boothReviewRepository.save(BoothReview.builder()
                .reviewer(user)
                .linkedBooth(booth)
                .star(request.star())
                .content(request.content())
                .imageUrl(image)
                .build()
        );
    }

    @Transactional
    public Slice<BoothReviewDto> getBoothReviews(long boothId, Pageable pageable){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }

        return boothReviewRepository.findBoothReviewsByLinkedBoothId(boothId, pageable).map(BoothReviewDto::of);
    }

    @Transactional(readOnly = true)
    public BoothReviewDto getBoothReview(long reviewId){
        return BoothReviewDto.of(getBoothReviewOrException(reviewId));
    }

    @Transactional
    public void deleteReview(long userId, long reviewId){
        BoothReview review = getBoothReviewOrException(reviewId);
        if(review.getReviewer().getId() != userId){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        boothReviewRepository.deleteById(reviewId);
    }

}
