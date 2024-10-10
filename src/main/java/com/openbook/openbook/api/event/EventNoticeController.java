package com.openbook.openbook.api.event;


import com.openbook.openbook.api.event.request.EventNoticeModifyRequest;
import com.openbook.openbook.api.event.request.EventNoticeRegisterRequest;
import com.openbook.openbook.api.event.response.EventNoticeResponse;
import com.openbook.openbook.service.event.EventNoticeService;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventNoticeController {

    private final EventNoticeService eventNoticeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{event_id}/notices")
    public ResponseMessage postNotice(Authentication authentication,
                                      @PathVariable Long event_id,
                                      @Valid EventNoticeRegisterRequest request) {
        eventNoticeService.registerEventNotice(Long.valueOf(authentication.getName()), event_id, request);
        return new ResponseMessage("공지 등록에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/{event_id}/notices")
    public SliceResponse<EventNoticeResponse> getEventNotices(@PathVariable Long event_id,
                                                              @PageableDefault(size = 5) Pageable pageable) {
        return SliceResponse.of(eventNoticeService.getEventNotices(event_id, pageable).map(EventNoticeResponse::of));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/events/notices/{notice_id}")
    public EventNoticeResponse getEventNotice(@PathVariable Long notice_id) {
        return EventNoticeResponse.of(eventNoticeService.getEventNotice(notice_id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/event/notices/{notice_id}")
    public ResponseMessage modifyNotice(Authentication authentication,
                                        @PathVariable Long notice_id,
                                        @NotNull EventNoticeModifyRequest request) {
        eventNoticeService.updateNotice(Long.parseLong(authentication.getName()), notice_id, request);
        return new ResponseMessage("공지 수정에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/events/notices/{notice_id}")
    public ResponseMessage deleteNotice(Authentication authentication,
                                        @PathVariable Long notice_id) {
        eventNoticeService.deleteEventNotice(Long.valueOf(authentication.getName()), notice_id);
        return new ResponseMessage("공지 삭제에 성공했습니다.");
    }

}
