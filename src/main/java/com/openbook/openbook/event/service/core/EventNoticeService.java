package com.openbook.openbook.event.service.core;


import com.openbook.openbook.event.dto.EventNoticeDto;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventNotice;
import com.openbook.openbook.event.repository.EventNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventNoticeService {

    private final EventNoticeRepository eventNoticeRepository;

    public void createEventNotice(EventNoticeDto eventNoticeDto) {
        eventNoticeRepository.save(
                EventNotice.builder()
                        .title(eventNoticeDto.title())
                        .content(eventNoticeDto.content())
                        .type(eventNoticeDto.type())
                        .imageUrl(eventNoticeDto.imageUrl())
                        .linkedEvent(eventNoticeDto.linkedEvent())
                        .build()
        );
    }

    public Slice<EventNotice> getNotices(Event event, Pageable pageable) {
        return eventNoticeRepository.findByLinkedEventId(event.getId(), pageable);
    }

}
