package com.openbook.openbook.event.service;


import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.repository.EventLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventLayoutService {

    private final EventLayoutRepository eventLayoutRepository;

    public void get() {
    }

}
