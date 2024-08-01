package com.openbook.openbook.event.service.core;

import com.openbook.openbook.event.dto.EventDTO;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event getEventOrException(final Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new OpenBookException(ErrorCode.EVENT_NOT_FOUND)
        );
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAllRequested(pageable);
    }

    public Slice<Event> getAllManagedEvents(Pageable pageable, Long managerId) {
        return eventRepository.findAllByManagerId(pageable, managerId);
    }

    public Page<Event> getAllEventsWithStatus(Pageable pageable, EventStatus status) {
        return eventRepository.findAllRequestedByStatus(pageable, status);
    }

    public Slice<Event> getEventsWithNameMatchBy(String name, EventStatus status, Pageable pageable) {
        return eventRepository.findAllByNameAndStatus(pageable, name, status);
    }

    public Slice<Event> getAllManagedEventsWithStatus(Pageable pageable, Long managerId, EventStatus status) {
        return eventRepository.findAllByManagerIdAndStatus(pageable, managerId, status);
    }

    public Slice<Event> getEventsWithProgress(Pageable pageable, String progress) {
        return switch (progress) {
            case "all" -> eventRepository.findAllApproved(pageable);
            case "ongoing" -> eventRepository.findAllOngoing(pageable);
            case "recruiting" -> eventRepository.findAllRecruiting(pageable);
            case "terminated" -> eventRepository.findAllTerminated(pageable);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }

    public Event createEvent(EventDTO event) {
        return eventRepository.save(
                Event.builder()
                        .manager(event.manager())
                        .layout(event.layout())
                        .location(event.location())
                        .name(event.name())
                        .mainImageUrl(event.mainImageUrl())
                        .description(event.description())
                        .openDate(event.openDate())
                        .closeDate(event.closeDate())
                        .boothRecruitmentStartDate(event.b_RecruitmentStartDate())
                        .boothRecruitmentEndDate(event.b_RecruitmentEndDate())
                        .build()
        );
    }

}
