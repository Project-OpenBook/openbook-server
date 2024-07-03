package com.openbook.openbook.event.service;

import com.openbook.openbook.event.dto.EventDTO;
import com.openbook.openbook.event.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event getEventOrException(final Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "행사가 존재하지 않습니다.")
        );
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAllRequested(pageable);
    }

    public Page<Event> getAllEventsWithStatus(Pageable pageable, EventStatus status) {
        return eventRepository.findAllRequestedByStatus(pageable, status);
    }

    public Slice<Event> getEventsWithProgress(Pageable pageable, String progress) {
        return switch (progress) {
            case "all" -> eventRepository.findAllApproved(pageable);
            case "ongoing" -> eventRepository.findAllOngoing(pageable);
            case "recruiting" -> eventRepository.findAllRecruiting(pageable);
            case "terminated" -> eventRepository.findAllTerminated(pageable);
            default -> throw new OpenBookException(HttpStatus.BAD_REQUEST, "요청 값이 잘못되었습니다.");
        };
    }

    public void createEvent(EventDTO event) {
        eventRepository.save(
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
