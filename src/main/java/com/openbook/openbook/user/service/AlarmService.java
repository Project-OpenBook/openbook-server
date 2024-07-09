package com.openbook.openbook.user.service;


import com.openbook.openbook.user.dto.AlarmDTO;
import com.openbook.openbook.user.dto.AlarmType;
import com.openbook.openbook.user.entity.Alarm;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public void createAlarm(User sender, User receiver, AlarmType type, String content) {
        alarmRepository.save(
                Alarm.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .alarmType(type.toString())
                        .content(content)
                        .message(type.getMessage())
                        .build()
        );
    }
}
