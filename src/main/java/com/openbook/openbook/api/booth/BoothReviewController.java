package com.openbook.openbook.api.booth;


import com.openbook.openbook.api.SliceResponse;
import com.openbook.openbook.api.booth.request.BoothReviewRegisterRequest;
import com.openbook.openbook.api.booth.response.BoothReviewResponse;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.service.booth.BoothReviewService;
import com.openbook.openbook.api.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoothReviewController {

    private final BoothReviewService boothReviewService;


    @PostMapping("/booths/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid BoothReviewRegisterRequest request){
        boothReviewService.registerBoothReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("부스 리뷰 작성에 성공했습니다."));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booth/reviews")
    public SliceResponse<BoothReviewResponse> getReviews(@RequestParam(value = "booth_id") Long boothId,
                                                         @PageableDefault(size = 5) Pageable pageable){
        return SliceResponse.of(boothReviewService.getBoothReviews(boothId, pageable).map(BoothReviewResponse::of));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/booth/reviews/{review_id}")
    public BoothReviewResponse getReview(@PathVariable Long review_id){
        return BoothReviewResponse.of(boothReviewService.getBoothReview(review_id));

    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/booth/reviews/{review_id}")
    public ResponseMessage deleteReview(Authentication authentication, @PathVariable Long review_id){
        boothReviewService.deleteReview(Long.parseLong(authentication.getName()), review_id);
        return new ResponseMessage("부스 리뷰 삭제에 성공했습니다.");
    }
}
