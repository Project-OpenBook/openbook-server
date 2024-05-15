package com.openbook.openbook.user;


import com.openbook.openbook.global.ResponseMessage;
import com.openbook.openbook.user.dto.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<ResponseMessage> signup(@RequestBody @Valid final SignUpRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(new ResponseMessage("API 요청 성공"));
    }

}
