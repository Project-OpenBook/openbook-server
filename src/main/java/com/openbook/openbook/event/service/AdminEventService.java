package com.openbook.openbook.event.service;


import com.openbook.openbook.event.controller.response.AdminEventData;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.event.service.core.EventTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.dto.AlarmType;
import com.openbook.openbook.user.service.core.AlarmService;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventService eventService;
    private final EventTagService eventTagService;
    private final AlarmService alarmService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AdminEventData> getRequestedEvents(Pageable pageable, String status) {
        Page<Event> events = (status.equals("all"))
                ? eventService.getAllEvents(pageable)
                : eventService.getAllEventsWithStatus(pageable, getEventStatus(status));
        return events.map(
                event -> AdminEventData.of(event, eventTagService.getEventTags(event.getId()))
        );
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
