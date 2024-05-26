package com.openbook.openbook.administrator;


import com.openbook.openbook.administrator.dto.AdminEventData;
import com.openbook.openbook.event.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Page<AdminEventData> getRequestedEvents(Pageable pageable, String status) {
        if(status.equals("all")) {
            return eventRepository
                    .findAllRequested(pageable)
                    .map(AdminEventData::of);
        }
        return eventRepository
                .findAllRequestedByStatus(pageable, getEventStatus(status))
                .map(AdminEventData::of);
    }

    @Transactional
    public void changeEventStatus(Long eventId, EventStatus status) {
        Event event = getEventOrException(eventId);
        event.updateStatus(status);
    }

    private Event getEventOrException(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "일치하는 행사가 존재하지 않습니다.")
        );
    }

    private EventStatus getEventStatus(String status) {
        return switch (status) {
            case "waiting" -> EventStatus.WAITING;
            case "approved" -> EventStatus.APPROVE;
            case "rejected" -> EventStatus.REJECT;
            default -> throw new OpenBookException(HttpStatus.BAD_REQUEST, "요청 값이 잘못되었습니다.");
        };
    }
}
