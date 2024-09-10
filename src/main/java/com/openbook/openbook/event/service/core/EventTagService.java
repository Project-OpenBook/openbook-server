package com.openbook.openbook.event.service.core;


import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.repository.EventTagRepository;
import com.openbook.openbook.global.util.TagUtil;
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
