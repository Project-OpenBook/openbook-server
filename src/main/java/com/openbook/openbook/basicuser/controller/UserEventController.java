package com.openbook.openbook.basicuser.controller;


import com.openbook.openbook.basicuser.dto.response.EventLayoutStatus;
import com.openbook.openbook.basicuser.service.UserEventService;
import com.openbook.openbook.basicuser.dto.request.EventRegistrationRequest;
import com.openbook.openbook.global.dto.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserEventController {

    private final UserEventService userEventService;

    @PostMapping("/events")
    public ResponseEntity<ResponseMessage> registration(Authentication authentication,
                                                        @Valid EventRegistrationRequest request) {
        userEventService.eventRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("신청이 완료되었습니다."));
    }

    @GetMapping("/events/{eventId}/layout/status")
    public ResponseEntity<EventLayoutStatus> getEventLayoutStatus(@PathVariable Long eventId) {
        return ResponseEntity.ok(userEventService.getEventLayoutStatus(eventId));
    }
}
