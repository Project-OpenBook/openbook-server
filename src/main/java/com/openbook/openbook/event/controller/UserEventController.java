package com.openbook.openbook.event.controller;


import com.openbook.openbook.event.controller.request.EventRegistrationRequest;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.global.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserEventController {

    private final EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<ResponseMessage> registration(Authentication authentication,
                                                        @Valid EventRegistrationRequest request) {
        eventService.eventRegistration(Long.valueOf(authentication.getName()), request);
        return ResponseEntity.ok(new ResponseMessage("행사 등록 요청 성공"));
    }

}
