package com.openbook.openbook.event.service.core;


import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.event.repository.EventTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventTagService {

    private final EventTagRepository eventTagRepository;

    public void createEventTag(String name, Event event) {
        eventTagRepository.save(
                EventTag.builder()
                        .name(name)
                        .linkedEvent(event)
                        .build()
        );
    }

    public List<EventTag> getEventTags(Long eventId) {
        return eventTagRepository.findAllByLinkedEventId(eventId);
    }
}
