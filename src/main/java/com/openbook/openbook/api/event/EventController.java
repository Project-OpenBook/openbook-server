package com.openbook.openbook.api.event;


import com.openbook.openbook.api.event.request.EventRegistrationRequest;
import com.openbook.openbook.api.event.request.EventStatusUpdateRequest;
import com.openbook.openbook.api.event.response.EventDetail;
import com.openbook.openbook.api.event.response.EventLayoutResponse;
import com.openbook.openbook.api.event.response.ManagerEventData;
import com.openbook.openbook.api.event.response.UserEventData;
import com.openbook.openbook.service.event.EventService;
import com.openbook.openbook.api.PageResponse;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public ResponseMessage registration(Authentication authentication,
                                        @Valid EventRegistrationRequest request) {
        eventService.createEvent(Long.valueOf(authentication.getName()), request);
        return new ResponseMessage("행사 신청이 완료되었습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events")
    public SliceResponse<UserEventData> getEvents(@RequestParam(defaultValue = "all") String progress,
                                                  @PageableDefault(size = 6) Pageable pageable) {
        return SliceResponse.of(eventService.getEventsByProgress(pageable, progress).map(UserEventData::of));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}")
    public EventDetail getEventDetail(@PathVariable Long eventId) {
        return EventDetail.of(eventService.getEventById(eventId), eventService.findBoothCount(eventId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{eventId}/layout/status")
    public EventLayoutResponse getEventLayoutStatus(@PathVariable Long eventId) {
        return EventLayoutResponse.of(eventService.getEventLayoutStatus(eventId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/search")
    public SliceResponse<UserEventData> searchEvents(@RequestParam(value = "type", defaultValue = "eventName") String searchType,
                                                     @RequestParam(value = "query", defaultValue = "") String name,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "sort", defaultValue = "desc") String sort) {
        return SliceResponse.of(
                eventService.getEventsSearchBy(searchType, name, page, sort)
                        .map(UserEventData::of)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/events")
    public SliceResponse<ManagerEventData> getManagedEvent(Authentication authentication,
                                                           @RequestParam(defaultValue = "ALL") String status,
                                                           @PageableDefault(size = 6) Pageable pageable ) {
        return SliceResponse.of(
                eventService.getEventsByManager(Long.valueOf(authentication.getName()), pageable, status)
                        .map(ManagerEventData::of)
        );
    }

    @PreAuthorize("authentication.name == '1'")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin/events")
    public PageResponse<ManagerEventData> getEventPage(@RequestParam(defaultValue = "all") String status,
                                                       @PageableDefault(size = 10) Pageable pageable) {
        return PageResponse.of(
                eventService.getEventsByStatus(pageable, status).map(ManagerEventData::of)
        );
    }

    @PreAuthorize("authentication.name == '1'")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/admin/events/{eventId}/status")
    public ResponseMessage changeEventStatus (@PathVariable Long eventId,
                                                              @RequestBody EventStatusUpdateRequest request) {
        eventService.changeEventStatus(eventId, request.status());
        return new ResponseMessage("행사 상태가 변경되었습니다.");
    }

}
