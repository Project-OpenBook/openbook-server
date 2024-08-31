package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.request.EventReviewRegisterRequest;
import com.openbook.openbook.event.controller.response.EventNoticeData;
import com.openbook.openbook.event.controller.response.UserEventData;
import com.openbook.openbook.event.controller.response.EventDetail;
import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.event.service.common.CommonEventReviewService;
import com.openbook.openbook.event.service.EventCommonService;
import com.openbook.openbook.event.controller.request.EventRegistrationRequest;
import com.openbook.openbook.event.service.EventLayoutCommonService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserEventController {

    private final EventCommonService eventCommonService;
    private final EventLayoutCommonService eventLayoutCommonService;
    private final CommonEventReviewService eventReviewService;

    @PostMapping("/events")
    public ResponseEntity<ResponseMessage> registration(Authentication authentication,
                                                        @Valid EventRegistrationRequest request) {
        eventCommonService.eventRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("신청이 완료되었습니다."));
    }

    @GetMapping("/events/{eventId}/layout/status")
    public ResponseEntity<EventLayoutStatus> getEventLayoutStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventLayoutCommonService.getEventLayoutStatus(eventId));
    }

    @GetMapping("/events")
    public ResponseEntity<SliceResponse<UserEventData>> getEvents(@RequestParam(defaultValue = "all") String progress,
                                                                  @PageableDefault(size = 6) Pageable pageable) {
        return ResponseEntity.ok(SliceResponse.of(eventCommonService.getEventBasicData(pageable, progress)));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDetail> getEvent(Authentication authentication, @PathVariable Long eventId) {
        return ResponseEntity.ok(eventCommonService.getEventDetail(Long.valueOf(authentication.getName()), eventId));
    }

    @GetMapping("/events/{event_id}/notices")
    public ResponseEntity<SliceResponse<EventNoticeData>> getEventNotices(@PathVariable Long event_id,
                                                                          @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(SliceResponse.of(eventCommonService.getEventNotices(event_id, pageable)));
    }

    @GetMapping("/events/notices/{notice_id}")
    public ResponseEntity<EventNoticeData> getEventNotice(@PathVariable Long notice_id) {
        return ResponseEntity.ok(eventCommonService.getEventNotice(notice_id));
    }


    @PostMapping("/event/review")
    public ResponseEntity<ResponseMessage> postReview(Authentication authentication,
                                                      @Valid EventReviewRegisterRequest request) {
        eventReviewService.registerEventReview(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("행사 리뷰 작성에 성공했습니다."));
    }


    @GetMapping("events/search")
    public ResponseEntity<SliceResponse<UserEventData>> searchEvents(@RequestParam(value = "type", defaultValue = "eventName") String searchType,
                                                                     @RequestParam(value = "query", defaultValue = "") String name,
                                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "sort", defaultValue = "desc") String sort) {
        Slice<UserEventData> result = eventCommonService.getEventsSearchBy(searchType, name, page, sort);
        return ResponseEntity.ok(SliceResponse.of(result));
    }

}
