package com.openbook.openbook.administrator;


import com.openbook.openbook.administrator.dto.AdminEventData;
import com.openbook.openbook.event.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.AlarmType;
import com.openbook.openbook.user.service.AlarmService;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventService eventService;
    private final AlarmService alarmService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AdminEventData> getRequestedEvents(Pageable pageable, String status) {
        if(status.equals("all")) {
            return eventService.getAllEvents(pageable).map(AdminEventData::of);
        }
        return eventService.getAllEventsWithStatus(pageable, getEventStatus(status))
                .map(AdminEventData::of);
    }

    @Transactional
    public void changeEventStatus(Long eventId, EventStatus status) {
        Event event = eventService.getEventOrException(eventId);
        event.updateStatus(status);
        AlarmType t = (status==EventStatus.APPROVE) ? AlarmType.EVENT_APPROVED : AlarmType.EVENT_REJECTED;
        alarmService.createAlarm(userService.getAdminOrException(), event.getManager(), t, event.getName());
    }

    private EventStatus getEventStatus(String status) {
        return switch (status) {
            case "waiting" -> EventStatus.WAITING;
            case "approved" -> EventStatus.APPROVE;
            case "rejected" -> EventStatus.REJECT;
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }

}
