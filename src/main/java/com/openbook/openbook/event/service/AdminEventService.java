package com.openbook.openbook.event.service;


import com.openbook.openbook.event.dto.AdminEventData;
import com.openbook.openbook.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository eventRepository;

    public Page<AdminEventData> getEventList(Pageable pageable) {
        return eventRepository.findAllRequest(pageable).map(AdminEventData::of);
    }
}
