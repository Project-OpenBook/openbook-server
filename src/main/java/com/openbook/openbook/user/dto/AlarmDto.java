package com.openbook.openbook.user.dto;

import com.openbook.openbook.user.entity.Alarm;
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
