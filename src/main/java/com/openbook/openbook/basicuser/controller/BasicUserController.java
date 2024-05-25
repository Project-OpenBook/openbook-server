package com.openbook.openbook.basicuser.controller;


import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.basicuser.service.BasicUserService;
import com.openbook.openbook.basicuser.dto.request.LoginRequest;
import com.openbook.openbook.basicuser.dto.request.SignUpRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BasicUserController {

    private final BasicUserService basicUserService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody @Valid final SignUpRequest request) {
        basicUserService.signup(request);
        return ResponseEntity.ok(new ResponseMessage("API 요청 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid final LoginRequest request) {
        String token = basicUserService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
