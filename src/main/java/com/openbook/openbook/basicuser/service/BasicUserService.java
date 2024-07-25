package com.openbook.openbook.basicuser.service;


import com.openbook.openbook.basicuser.dto.response.AlarmData;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.util.JwtUtils;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.UserDTO;
import com.openbook.openbook.basicuser.dto.request.LoginRequest;
import com.openbook.openbook.basicuser.dto.request.SignUpRequest;
import com.openbook.openbook.user.entity.Alarm;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.AlarmService;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final AlarmService alarmService;

    @Transactional
    public void signup(final SignUpRequest request) {
        if(userService.getUserByEmail(request.email()).isPresent()) {
            throw new OpenBookException(ErrorCode.DUPLICATE_EMAIL);
        }
        UserDTO user = UserDTO.builder()
                .email(request.email())
                .name(request.name())
                .nickname(request.nickname())
                .password(encoder.encode(request.password()))
                .build();

        userService.createUser(user);
    }

    @Transactional(readOnly = true)
    public String login(final LoginRequest request) {
        User user = userService.getUserByEmailOrException(request.email());
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new OpenBookException(ErrorCode.INVALID_PASSWORD);
        }
        return jwtUtils.generateToken(user.getId());
    }

    @Transactional(readOnly = true)
    public Slice<AlarmData> getAlarmData(Pageable pageable, final Long id) {
        User user = userService.getUserOrException(id);
        return alarmService.getUserReceivedAlarm(pageable, user).map(AlarmData::of);
    }

    @Transactional
    public void deleteAlarm(final Long userId, final Long alarmId) {
        Alarm alarm = alarmService.getAlarmOrException(alarmId);
        if(alarm.getReceiver().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        alarmService.deleteAlarm(alarm);
    }

    @Transactional
    public void deleteAllAlarm(final Long userId) {
        User user = userService.getUserOrException(userId);
        if(!alarmService.userHasReceivedAlarms(userId)) {
            throw new OpenBookException(ErrorCode.ALARM_NOT_FOUND);
        }
        alarmService.deleteAllReceiverAlarm(user.getId());
    }

}
