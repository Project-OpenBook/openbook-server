package com.openbook.openbook.service.user.dto;

import com.openbook.openbook.domain.user.Alarm;
import java.time.LocalDateTime;

public record AlarmDto(
        long id,
        UserDto receiver,
        UserDto sender,
        String alarmType,
        String content,
        String message,
        LocalDateTime registeredAt
) {
    public static AlarmDto of(Alarm alarm) {
        return new AlarmDto(
                alarm.getId(),
                UserDto.of(alarm.getReceiver()),
                UserDto.of(alarm.getSender()),
                alarm.getAlarmType(),
                alarm.getContent(),
                alarm.getMessage(),
                alarm.getRegisteredAt()
        );
    }
}
