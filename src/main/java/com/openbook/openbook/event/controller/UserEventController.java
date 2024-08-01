package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.response.UserEventData;
import com.openbook.openbook.event.controller.response.EventDetail;
import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.event.service.UserEventService;
import com.openbook.openbook.event.controller.request.EventRegistrationRequest;
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

    private final UserEventService userEventService;

    @PostMapping("/events")
    public ResponseEntity<ResponseMessage> registration(Authentication authentication,
                                                        @Valid EventRegistrationRequest request) {
        userEventService.eventRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("신청이 완료되었습니다."));
    }

    @GetMapping("/events/{eventId}/layout/status")
    public ResponseEntity<EventLayoutStatus> getEventLayoutStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(userEventService.getEventLayoutStatus(eventId));
    }

    @GetMapping("/events")
    public ResponseEntity<SliceResponse<UserEventData>> getEvents(@RequestParam(defaultValue = "all") String progress,
                                                                  @PageableDefault(size = 6) Pageable pageable) {
        return ResponseEntity.ok(SliceResponse.of(userEventService.getEventBasicData(pageable, progress)));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDetail> getEvent(Authentication authentication, @PathVariable Long eventId) {
        return ResponseEntity.ok(userEventService.getEventDetail(Long.valueOf(authentication.getName()), eventId));
    }

    @GetMapping("events/search")
    public ResponseEntity<SliceResponse<UserEventData>> searchEvents(@PageableDefault(size = 6) Pageable pageable,
                                             @RequestParam(value = "type", defaultValue = "eventName") String searchType,
                                             @RequestParam(value = "query", defaultValue = "") String name) {
        Slice<UserEventData> result = userEventService.getEventsSearchBy(pageable, searchType, name);
        return ResponseEntity.ok(SliceResponse.of(result));
    }

}
