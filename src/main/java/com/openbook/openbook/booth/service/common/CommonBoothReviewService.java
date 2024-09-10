package com.openbook.openbook.booth.service.common;

import com.openbook.openbook.booth.controller.request.BoothReviewRegisterRequest;
import com.openbook.openbook.booth.dto.BoothReviewDto;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.core.BoothReviewService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommonBoothReviewService {
    
    private final UserService userService;
    private final BoothService boothService;
    private final BoothReviewService boothReviewService;

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

        BoothReviewDto reviewDto = new BoothReviewDto(user, booth, request.star(), request.content(), request.image());
        boothReviewService.createBoothReview(reviewDto);
    }
}
