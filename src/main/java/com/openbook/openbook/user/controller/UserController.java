package com.openbook.openbook.user.controller;


import com.openbook.openbook.user.controller.response.AlarmData;
import com.openbook.openbook.global.dto.ResponseMessage;
import com.openbook.openbook.user.service.UserCommonService;
import com.openbook.openbook.user.controller.request.LoginRequest;
import com.openbook.openbook.user.controller.request.SignUpRequest;
import com.openbook.openbook.global.dto.SliceResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommonService basicUserService;

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

    @GetMapping("/alarms")
    public ResponseEntity<SliceResponse<AlarmData>> getAlarms(@PageableDefault(size = 5) Pageable pageable,
                                                              Authentication authentication) {
        return ResponseEntity.ok(
                SliceResponse.of(
                        basicUserService.getAlarmData(pageable, Long.valueOf(authentication.getName()))
                )
        );
    }

    @DeleteMapping("/alarms")
    public ResponseEntity<ResponseMessage> deleteAllAlarm(Authentication authentication) {
        basicUserService.deleteAllAlarm(Long.valueOf(authentication.getName()));
        return ResponseEntity.ok(new ResponseMessage("전체 알림을 삭제했습니다."));
    }

    @DeleteMapping("/alarms/{alarmId}")
    public ResponseEntity<ResponseMessage> deleteAlarm(Authentication authentication, @PathVariable Long alarmId) {
        basicUserService.deleteAlarm(Long.valueOf(authentication.getName()), alarmId);
        return ResponseEntity.ok(new ResponseMessage("알림을 삭제했습니다."));
    }

}
