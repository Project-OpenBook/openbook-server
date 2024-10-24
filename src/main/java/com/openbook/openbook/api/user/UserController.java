package com.openbook.openbook.api.user;


import com.openbook.openbook.api.user.request.UserModifyRequest;
import com.openbook.openbook.api.user.response.UserProfileResponse;
import com.openbook.openbook.util.TokenProvider;
import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.user.response.TokenInfo;
import com.openbook.openbook.api.user.request.LoginRequest;
import com.openbook.openbook.api.user.request.SignUpRequest;
import com.openbook.openbook.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    final private TokenProvider tokenProvider;
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("user/access_token_info")
    public TokenInfo getTokenInfo(@NotNull HttpServletRequest request) {
        String token = tokenProvider.getTokenFrom(request);
        return tokenProvider.getInfoOf(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseMessage signup(@RequestBody @Valid final SignUpRequest request) {
        userService.signup(request);
        return new ResponseMessage("API 요청 성공");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid final LoginRequest request) {
        String token = userService.login(request);
        return Map.of("token", token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manage/profile")
    public UserProfileResponse getProfile(Authentication authentication){
        return UserProfileResponse.of(userService.getUserProfile(Long.valueOf(authentication.getName())));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/manage/profile")
    public ResponseMessage modifyUser(Authentication authentication,
                                      @NotNull UserModifyRequest request){
        userService.modifyUser(Long.parseLong(authentication.getName()), request);
        return new ResponseMessage("프로필 수정에 성공했습니다.");
    }

}
