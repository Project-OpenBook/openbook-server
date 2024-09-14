package com.openbook.openbook.service.event;


import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.event.EventTag;
import com.openbook.openbook.domain.event.dto.EventStatus;
import com.openbook.openbook.repository.event.EventTagRepository;
import com.openbook.openbook.util.TagUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventTagService {

    private final EventTagRepository eventTagRepository;

    public void createEventTags(List<String> names, Event event) {
        TagUtil.getValidTagsOrException(names).forEach(
                name ->  eventTagRepository.save(EventTag.builder()
                        .name(name)
                        .linkedEvent(event)
                        .build()
                )
        );
    }

    public Slice<Event> getEventsWithTagNameMatchBy(String name, EventStatus status, Pageable pageable) {
        return eventTagRepository.findLinkedEventsByNameAndEventStatus(pageable, name, status);
    }

}
