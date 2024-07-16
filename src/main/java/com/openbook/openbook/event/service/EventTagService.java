package com.openbook.openbook.event.service;


import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.event.repository.EventTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventTagService {

    private final EventTagRepository eventTagRepository;

    public void createEventTag(String content, Event event) {
        eventTagRepository.save(
                EventTag.builder()
                        .content(content)
                        .linkedEvent(event)
                        .build()
        );
    }
}
