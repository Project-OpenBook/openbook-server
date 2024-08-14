package com.openbook.openbook.event.controller;

import com.openbook.openbook.event.controller.response.ManagerEventData;
import com.openbook.openbook.event.service.ManagerEventService;
import com.openbook.openbook.global.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
}
