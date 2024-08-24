package com.openbook.openbook.event.service;

import com.openbook.openbook.event.controller.request.EventNoticeRegisterRequest;
import com.openbook.openbook.event.controller.response.ManagerEventData;
import com.openbook.openbook.event.dto.EventNoticeDto;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventNotice;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.service.core.EventNoticeService;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.event.service.core.EventTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
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
    private final EventNoticeService eventNoticeService;


    @Transactional(readOnly = true)
    public Slice<ManagerEventData> getManagedEventList(Long managerId, Pageable pageable, String status) {
        userService.getUserOrException(managerId);
        Slice<Event> events = (status.equals("ALL"))
                ? eventService.getAllManagedEvents(pageable, managerId)
                : eventService.getAllManagedEventsWithStatus(pageable, managerId, EventStatus.valueOf(status));
        return events.map(
                event -> ManagerEventData.of(event, eventTagService.getEventTags(event.getId()))
        );
    }

    @Transactional
    public void registerEventNotice(Long userId, Long eventId, EventNoticeRegisterRequest request) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getManager().getId().equals(userId)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        eventNoticeService.createEventNotice(new EventNoticeDto(
                request.title(), request.content(), request.image(),
                request.noticeType(), event)
        );
    }

    @Transactional
    public void deleteEventNotice(Long userId, Long noticeId) {
        EventNotice eventNotice = eventNoticeService.getEventNoticeOrException(noticeId);
        if (!eventNotice.getLinkedEvent().getManager().getId().equals(userId)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        eventNoticeService.deleteEventNotice(eventNotice);
    }

}
