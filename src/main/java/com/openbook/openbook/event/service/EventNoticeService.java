package com.openbook.openbook.event.service;


import com.openbook.openbook.event.controller.request.EventNoticeRegisterRequest;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventNoticeService {
    private final EventService eventService;
    private final EventNoticeRepository eventNoticeRepository;
    private final S3Service s3Service;


    public EventNotice getEventNoticeOrException(final Long id) {
        return eventNoticeRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.EVENT_NOTICE_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public EventNoticeDto getEventNotice(final Long noticeId) {
        return EventNoticeDto.of(getEventNoticeOrException(noticeId));
    }

    @Transactional(readOnly = true)
    public Slice<EventNoticeDto> getEventNotices(final Long eventId, Pageable pageable) {
        Event event = eventService.getEventOrException(eventId);
        return eventNoticeRepository.findByLinkedEventId(event.getId(), pageable).map(EventNoticeDto::of);
    }

    @Transactional
    public void registerEventNotice(Long userId, Long eventId, EventNoticeRegisterRequest request) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getManager().getId().equals(userId)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        eventNoticeRepository.save(EventNotice.builder()
                .title(request.title())
                .content(request.content())
                .type(request.noticeType())
                .imageUrl(s3Service.uploadFileAndGetUrl(request.image()))
                .linkedEvent(event)
                .build()
        );
    }

    @Transactional
    public void deleteEventNotice(Long userId, Long noticeId) {
        EventNotice eventNotice = getEventNoticeOrException(noticeId);
        if (!eventNotice.getLinkedEvent().getManager().getId().equals(userId)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        s3Service.deleteFileFromS3(eventNotice.getImageUrl());
        eventNoticeRepository.delete(eventNotice);
    }


}
