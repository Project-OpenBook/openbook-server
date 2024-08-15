package com.openbook.openbook.event.controller;

import com.openbook.openbook.event.controller.request.NoticeRegisterRequest;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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
                                                        @Valid NoticeRegisterRequest request) {
        managerEventService.registerEventNotice(Long.valueOf(authentication.getName()), event_id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("공지 등록에 성공했습니다."));
    }
}
