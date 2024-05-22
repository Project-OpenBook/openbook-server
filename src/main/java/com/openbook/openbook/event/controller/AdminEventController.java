package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.dto.AdminEventData;
import com.openbook.openbook.event.service.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/events")
    public ResponseEntity<Page<AdminEventData>> eventList(Pageable pageable) {
        return ResponseEntity.ok(adminEventService.getEventList(pageable));
    }
}
