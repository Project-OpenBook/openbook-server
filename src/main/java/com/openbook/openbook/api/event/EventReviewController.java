package com.openbook.openbook.api.event;

import com.openbook.openbook.api.event.request.EventReviewModifyRequest;
import com.openbook.openbook.api.event.request.EventReviewRegisterRequest;
import com.openbook.openbook.api.event.response.EventReviewResponse;
import com.openbook.openbook.service.event.EventReviewService;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventReviewController {

    private final EventReviewService eventReviewService;

    @PostMapping("/event/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid EventReviewRegisterRequest request) {
        eventReviewService.registerEventReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("행사 리뷰 작성에 성공했습니다."));
    }

    @GetMapping("/event/reviews")
    public SliceResponse<EventReviewResponse> getReviews(@RequestParam(value = "event_id") Long request,
                                                         @PageableDefault(size = 5) Pageable pageable) {
        return SliceResponse.of(eventReviewService.getEventReviews(request, pageable).map(EventReviewResponse::of));
    }

    @GetMapping("/event/review/{review_id}")
    public ResponseEntity<EventReviewResponse> getReview(@PathVariable Long review_id) {
        return ResponseEntity.ok(EventReviewResponse.of(eventReviewService.getEventReview(review_id)));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/event/reviews/{review_id}")
    public ResponseMessage modifyReview(Authentication authentication,
                                        @PathVariable Long review_id,
                                        @Valid EventReviewModifyRequest request) {
        eventReviewService.modifyReview(Long.parseLong(authentication.getName()), review_id, request);
        return new ResponseMessage("리뷰 수정에 성공했습니다.");
    }

    @DeleteMapping("/event/reviews/{review_id}")
    public ResponseEntity<ResponseMessage> deleteReview(Authentication authentication, @PathVariable Long review_id) {
        eventReviewService.deleteReview(Long.parseLong(authentication.getName()), review_id);
        return ResponseEntity.ok(new ResponseMessage("리뷰 삭제에 성공했습니다."));
    }

}
