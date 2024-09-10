package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.request.EventRegistrationRequest;
import com.openbook.openbook.event.controller.request.EventStatusUpdateRequest;
import com.openbook.openbook.event.controller.response.EventDetail;
import com.openbook.openbook.event.controller.response.ManagerEventData;
import com.openbook.openbook.event.controller.response.UserEventData;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.global.dto.PageResponse;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<ResponseMessage> registration(Authentication authentication,
                                                        @Valid EventRegistrationRequest request) {
        eventService.createEvent(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("신청이 완료되었습니다."));
    }

    @GetMapping("/events")
    public SliceResponse<UserEventData> getEvents(@RequestParam(defaultValue = "all") String progress,
                                                  @PageableDefault(size = 6) Pageable pageable) {
        return SliceResponse.of(eventService.getEventsByProgress(pageable, progress).map(UserEventData::of));
    }

    @GetMapping("/events/{eventId}")
    public EventDetail getEventDetail(@PathVariable Long eventId) {
        return EventDetail.of(eventService.getEventById(eventId), eventService.findBoothCount(eventId));
    }


    @GetMapping("/events/search")
    public SliceResponse<UserEventData> searchEvents(@RequestParam(value = "type", defaultValue = "eventName") String searchType,
                                                     @RequestParam(value = "query", defaultValue = "") String name,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "sort", defaultValue = "desc") String sort) {
        Slice<UserEventData> result = eventService.getEventsSearchBy(searchType, name, page, sort).map(UserEventData::of);
        return SliceResponse.of(result);
    }

    @GetMapping("/manage/events")
    public SliceResponse<ManagerEventData> getManagedEvent(Authentication authentication,
                                                           @PageableDefault(size = 6) Pageable pageable,
                                                           @RequestParam(defaultValue = "ALL") String status) {
        return SliceResponse.of(
                eventService.getEventsByManager(Long.valueOf(authentication.getName()), pageable, status)
                        .map(ManagerEventData::of)
        );
    }

    //ADMIN
    @PreAuthorize("authentication.name == '1'")
    @GetMapping("/admin/events")
    public PageResponse<ManagerEventData> getEventPage(@RequestParam(defaultValue = "all") String status,
                                                       @PageableDefault(size = 10) Pageable pageable) {
        return PageResponse.of(eventService.getEventsByStatus(pageable, status).map(ManagerEventData::of));
    }

    @PreAuthorize("authentication.name == '1'")
    @PutMapping("/admin/events/{eventId}/status")
    public ResponseEntity<ResponseMessage> changeEventStatus (@PathVariable Long eventId,
                                                              @RequestBody EventStatusUpdateRequest request) {
        eventService.changeEventStatus(eventId, request.status());
        return ResponseEntity.ok(new ResponseMessage("행사 상태가 변경되었습니다."));
    }


}
