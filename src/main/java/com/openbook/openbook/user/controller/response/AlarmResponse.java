package com.openbook.openbook.user.controller.response;

import com.openbook.openbook.user.dto.AlarmDto;
import java.time.LocalDateTime;

public record AlarmResponse(
        Long id,
        String type,
        String content_name,
        String message,
        UserPublicResponse sender,
        LocalDateTime registeredAt
) {
    public static AlarmResponse of(AlarmDto alarm){
        return new AlarmResponse(
                alarm.id(),
                alarm.alarmType(),
                alarm.content(),
                alarm.message(),
                UserPublicResponse.of(alarm.sender()),
                alarm.registeredAt()
        );
    }
}
