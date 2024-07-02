package com.openbook.openbook.event.service;


import com.openbook.openbook.event.dto.EventLayoutDTO;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventLayoutService {

    private final EventLayoutRepository eventLayoutRepository;

    public EventLayout createEventLayout(final EventLayoutDTO eventLayout) {
        return eventLayoutRepository.save(
                EventLayout.builder()
                        .type(eventLayout.type())
                        .imageUrl(eventLayout.imageUrl())
                        .build()
        );
    }

}
