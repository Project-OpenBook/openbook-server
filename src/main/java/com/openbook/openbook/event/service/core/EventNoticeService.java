package com.openbook.openbook.event.service.core;


import com.openbook.openbook.event.dto.EventNoticeDto;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventNotice;
import com.openbook.openbook.event.repository.EventNoticeRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventNoticeService {

    private final EventNoticeRepository eventNoticeRepository;
    private final S3Service s3Service;

    public EventNotice getEventNoticeOrException(final Long id) {
        return eventNoticeRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.EVENT_NOTICE_NOT_FOUND)
        );
    }

    public void createEventNotice(EventNoticeDto eventNoticeDto) {
        eventNoticeRepository.save(
                EventNotice.builder()
                        .title(eventNoticeDto.title())
                        .content(eventNoticeDto.content())
                        .type(eventNoticeDto.type())
                        .imageUrl(s3Service.uploadFileAndGetUrl(eventNoticeDto.image()))
                        .linkedEvent(eventNoticeDto.linkedEvent())
                        .build()
        );
    }

    public Slice<EventNotice> getNotices(Event event, Pageable pageable) {
        return eventNoticeRepository.findByLinkedEventId(event.getId(), pageable);
    }

}
