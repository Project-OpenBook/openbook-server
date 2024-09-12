package com.openbook.openbook.booth.controller;


import com.openbook.openbook.booth.controller.request.BoothReviewRegisterRequest;
import com.openbook.openbook.booth.service.BoothReviewService;
import com.openbook.openbook.global.dto.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoothReviewController {

    private final BoothReviewService boothReviewService;


    @PostMapping("/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid BoothReviewRegisterRequest request){
        boothReviewService.registerBoothReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("부스 리뷰 작성에 성공했습니다."));
    }
}
