package com.openbook.openbook.event.service;

import com.openbook.openbook.event.controller.response.ManagerEventData;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.event.service.core.EventTagService;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerEventService {

    private final UserService userService;
    private final EventService eventService;
    private final EventTagService eventTagService;


    @Transactional(readOnly = true)
    public Slice<ManagerEventData> getManagedEventList(Long managerId, Pageable pageable, String status) {
        userService.getUserOrException(managerId);
        Slice<Event> events = (status.equals("all"))
                ? eventService.getAllManagedEvents(pageable, managerId)
                : eventService.getAllManagedEventsWithStatus(pageable, managerId, EventStatus.valueOf(status));
        return events.map(
                event -> ManagerEventData.of(event, eventTagService.getEventTags(event.getId()))
        );
    }

}
