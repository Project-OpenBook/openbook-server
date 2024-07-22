package com.openbook.openbook.user.service;


import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.AlarmType;
import com.openbook.openbook.user.entity.Alarm;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Alarm getAlarmOrException(Long id) {
        return alarmRepository.findById(id).orElseThrow(()->
                new OpenBookException(ErrorCode.ALARM_NOT_FOUND)
        );
    }

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

    public Slice<Alarm> getUserReceivedAlarm(Pageable pageable, User receiver) {
        return alarmRepository.findAllByReceiver(pageable, receiver);
    }

    public void deleteAlarm(Alarm alarm) {
        alarmRepository.delete(alarm);
    }

    public void deleteAllReceiverAlarm(Long id) {
        alarmRepository.deleteAllByReceiverId(id);
    }
}
