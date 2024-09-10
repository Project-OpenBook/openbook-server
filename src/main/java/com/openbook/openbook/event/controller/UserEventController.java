package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.request.EventReviewRegisterRequest;
import com.openbook.openbook.event.controller.response.EventReviewResponse;
import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.event.service.EventLayoutService;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.event.service.common.CommonEventReviewService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserEventController {

    private final EventService eventService;
    private final EventLayoutService eventLayoutService;
    private final CommonEventReviewService eventReviewService;



    @GetMapping("/events/{eventId}/layout/status")
    public ResponseEntity<EventLayoutStatus> getEventLayoutStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventLayoutService.getEventLayoutStatus(eventId));
    }




    @PostMapping("/event/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid EventReviewRegisterRequest request) {
        eventReviewService.registerEventReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("행사 리뷰 작성에 성공했습니다."));
    }

    @GetMapping("/event/reviews")
    public ResponseEntity<SliceResponse<EventReviewResponse>> getReviews(@RequestParam(value = "event_id") Long request,
                                                                         @PageableDefault(size = 5)Pageable pageable) {
        Slice<EventReviewResponse> reviews = eventReviewService.getEventReviews(request, pageable);
        return ResponseEntity.ok(SliceResponse.of(reviews));
    }

    @GetMapping("/event/review/{review_id}")
    public ResponseEntity<EventReviewResponse> getReview(@PathVariable Long review_id) {
        return ResponseEntity.ok(eventReviewService.getEventReview(review_id));
    }

    @DeleteMapping("/event/reviews/{review_id}")
    public ResponseEntity<ResponseMessage> deleteReview(Authentication authentication, @PathVariable Long review_id) {
        eventReviewService.deleteReview(Long.parseLong(authentication.getName()), review_id);
        return ResponseEntity.ok(new ResponseMessage("리뷰 삭제에 성공했습니다."));
    }



}
