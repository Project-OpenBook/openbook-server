package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.event.service.EventLayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserEventController {

    private final EventLayoutService eventLayoutService;



    @GetMapping("/events/{eventId}/layout/status")
    public ResponseEntity<EventLayoutStatus> getEventLayoutStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventLayoutService.getEventLayoutStatus(eventId));
    }








}
