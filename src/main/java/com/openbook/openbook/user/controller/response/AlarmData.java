package com.openbook.openbook.user.controller.response;

import com.openbook.openbook.user.dto.UserPublicData;
import com.openbook.openbook.user.entity.Alarm;
import java.time.LocalDateTime;

public record AlarmData(
        Long id,
        String type,
        String content_name,
        String message,
        UserPublicData sender,
        LocalDateTime registeredAt
) {
    public static AlarmData of(Alarm alarm){
        return new AlarmData(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getContent(),
                alarm.getMessage(),
                UserPublicData.of(alarm.getSender()),
                alarm.getRegisteredAt()
        );
    }
}
