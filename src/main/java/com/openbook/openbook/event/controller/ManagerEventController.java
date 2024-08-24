package com.openbook.openbook.event.controller;

import com.openbook.openbook.event.controller.request.EventNoticeRegisterRequest;
import com.openbook.openbook.event.controller.response.ManagerEventData;
import com.openbook.openbook.event.service.ManagerEventService;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
public class ManagerEventController {

    private final ManagerEventService managerEventService;

    @GetMapping("manage/events")
    public ResponseEntity<SliceResponse<ManagerEventData>> getManagedEvent(Authentication authentication,
                                                                   @PageableDefault(size = 6) Pageable pageable,
                                                                   @RequestParam(defaultValue = "ALL") String status) {
        return ResponseEntity.ok(SliceResponse.of(
                managerEventService.getManagedEventList(Long.valueOf(authentication.getName()), pageable, status))
        );
    }

    @PostMapping("/events/{event_id}/notices")
    public ResponseEntity<ResponseMessage> postNotice(Authentication authentication,
                                                        @PathVariable Long event_id,
                                                        @Valid EventNoticeRegisterRequest request) {
        managerEventService.registerEventNotice(Long.valueOf(authentication.getName()), event_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("공지 등록에 성공했습니다."));
    }

    @DeleteMapping("/events/notices/{notice_id}")
    public ResponseEntity<ResponseMessage> deleteNotice(Authentication authentication,
                                                        @PathVariable Long notice_id) {
        managerEventService.deleteEventNotice(Long.valueOf(authentication.getName()), notice_id);
        return ResponseEntity.ok(new ResponseMessage("공지 삭제에 성공했습니다."));
    }

}
