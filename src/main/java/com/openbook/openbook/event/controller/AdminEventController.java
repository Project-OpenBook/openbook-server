package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.dto.AdminEventData;
import com.openbook.openbook.event.controller.response.PageResponse;
import com.openbook.openbook.event.service.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/events")
    public ResponseEntity<PageResponse<AdminEventData>> getEventPage(
            @RequestParam(defaultValue = "all") String status, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(PageResponse.of(adminEventService.getRequestedEvents(pageable, status)));
    }

}



