package com.openbook.openbook.service.user;


import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.user.dto.AlarmDto;
import com.openbook.openbook.domain.user.dto.AlarmType;
import com.openbook.openbook.domain.user.Alarm;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.repository.user.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserService userService;
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

    @Transactional(readOnly = true)
    public Slice<AlarmDto> getAlarmData(Pageable pageable, final Long id) {
        User user = userService.getUserOrException(id);
        return alarmRepository.findAllByReceiver(pageable, user).map(AlarmDto::of);
    }

    @Transactional
    public void deleteAlarm(final Long userId, final Long alarmId) {
        Alarm alarm = getAlarmOrException(alarmId);
        if(alarm.getReceiver().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        alarmRepository.delete(alarm);
    }

    @Transactional
    public void deleteAllAlarm(final Long userId) {
        User user = userService.getUserOrException(userId);
        if(!userHasReceivedAlarms(userId)) {
            throw new OpenBookException(ErrorCode.ALARM_NOT_FOUND);
        }
        alarmRepository.deleteAllByReceiverId(user.getId());
    }

    private boolean userHasReceivedAlarms(Long receiverId) {
        return alarmRepository.existsByReceiverId(receiverId);
    }

}
