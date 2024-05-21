package com.openbook.openbook.booth.controller;

import com.openbook.openbook.booth.dto.request.BoothCreateRequest;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.global.ResponseMessage;
import com.openbook.openbook.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booths")
public class BoothController {

    private final BoothService boothService;
    @PostMapping
    public ResponseEntity <ResponseMessage>  createBooth(Authentication authentication, @Valid BoothCreateRequest request){
        boothService.createBooth(Long.valueOf(authentication.getName()), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseMessage("신청 완료 되었습니다."));

    }
}
