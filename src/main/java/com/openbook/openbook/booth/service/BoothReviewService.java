package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.request.BoothReviewRegisterRequest;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.dto.BoothReviewDto;
import com.openbook.openbook.booth.entity.BoothReview;
import com.openbook.openbook.booth.repository.BoothReviewRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothReviewService {
    private final BoothReviewRepository boothReviewRepository;
    private final S3Service s3Service;

    private final UserService userService;
    private final BoothService boothService;

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
        boothReviewRepository.save(BoothReview.builder()
                .reviewer(user)
                .linkedBooth(booth)
                .star(request.star())
                .content(request.content())
                .imageUrl(s3Service.uploadFileAndGetUrl(request.image()))
                .build()
        );
    }

}
