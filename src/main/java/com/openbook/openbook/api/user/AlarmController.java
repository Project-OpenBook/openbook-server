package com.openbook.openbook.api.user;


import com.openbook.openbook.api.ResponseMessage;
import com.openbook.openbook.api.SliceResponse;
import com.openbook.openbook.api.user.response.AlarmResponse;
import com.openbook.openbook.service.user.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/alarms")
    public SliceResponse<AlarmResponse> getAlarms(Authentication authentication,
                                                  @PageableDefault(size = 5) Pageable pageable) {
        return SliceResponse.of(alarmService.getAlarmData(
                pageable, Long.valueOf(authentication.getName())).map(AlarmResponse::of)
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/alarms")
    public ResponseMessage deleteAllAlarm(Authentication authentication) {
        alarmService.deleteAllAlarm(Long.valueOf(authentication.getName()));
        return new ResponseMessage("전체 알림을 삭제했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/alarms/{alarmId}")
    public ResponseMessage deleteAlarm(Authentication authentication,
                                       @PathVariable Long alarmId) {
        alarmService.deleteAlarm(Long.valueOf(authentication.getName()), alarmId);
        return new ResponseMessage("알림을 삭제했습니다.");
    }

}
