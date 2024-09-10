package com.openbook.openbook.user.controller;


import com.openbook.openbook.global.util.TokenProvider;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.user.controller.response.TokenInfo;
import com.openbook.openbook.user.controller.request.LoginRequest;
import com.openbook.openbook.user.controller.request.SignUpRequest;
import com.openbook.openbook.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    final private TokenProvider tokenProvider;
    private final UserService userService;

    @GetMapping("user/access_token_info")
    public ResponseEntity<TokenInfo> getTokenInfo(@NotNull HttpServletRequest request) {
        String token = tokenProvider.getTokenFrom(request);
        return ResponseEntity.ok(tokenProvider.getInfoOf(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody @Valid final SignUpRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(new ResponseMessage("API 요청 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid final LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }



}
