package com.openbook.openbook.service.event;


import com.openbook.openbook.api.event.request.EventNoticeModifyRequest;
import com.openbook.openbook.api.event.request.EventNoticeRegisterRequest;
import com.openbook.openbook.service.event.dto.EventNoticeDto;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.domain.event.EventNotice;
import com.openbook.openbook.repository.event.EventNoticeRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.event.dto.EventNoticeUpdateData;
import com.openbook.openbook.util.S3Service;
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
                .imageUrl((request.image()!=null)?s3Service.uploadFileAndGetUrl(request.image()):null)
                .linkedEvent(event)
                .build()
        );
    }

    @Transactional
    public void updateNotice(long userId, long noticeId, EventNoticeModifyRequest request) {
        EventNotice notice = getEventNoticeOrException(noticeId);
        if(!notice.getLinkedEvent().getManager().getId().equals(userId)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        notice.updateNotice(EventNoticeUpdateData.builder()
                        .title(request.title())
                        .content(request.content())
                        .type(request.noticeType())
                        .imageUrl((request.image()!=null)?s3Service.uploadFileAndGetUrl(request.image()):null)
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
