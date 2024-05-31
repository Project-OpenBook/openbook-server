package com.openbook.openbook.eventmanager;

import com.openbook.openbook.eventmanager.dto.BoothManageData;
import com.openbook.openbook.eventmanager.dto.BoothStatusUpdateRequest;
import com.openbook.openbook.global.dto.PageResponse;
import com.openbook.openbook.global.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EventManagerController {

    private final EventManagerService eventManagerService;
    @GetMapping("/events/{eventId}/managed/booths")
    public ResponseEntity<PageResponse<BoothManageData>> getBoothManagePage(@RequestParam(defaultValue = "all") String status,
                                                                            @PathVariable Long eventId,
                                                                            @PageableDefault(size = 10) Pageable pageable,
                                                                            Authentication authentication){
        return ResponseEntity.ok(PageResponse.of(eventManagerService.getBoothManageData(status, eventId, pageable, Long.valueOf(authentication.getName()))));
    }

    @PutMapping("/events/booths/{boothId}/status")
    public ResponseEntity<ResponseMessage> changeBoothStatus(@PathVariable Long boothId,
                                                             @RequestBody BoothStatusUpdateRequest request, Authentication authentication) {
        eventManagerService.changeBoothStatus(boothId, request.boothStatus(), Long.valueOf(authentication.getName()));
        return ResponseEntity.ok(new ResponseMessage("부스 상태가 변경되었습니다."));
    }
}
