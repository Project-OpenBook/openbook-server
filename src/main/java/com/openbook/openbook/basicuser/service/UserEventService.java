package com.openbook.openbook.basicuser.service;


import com.openbook.openbook.basicuser.dto.request.EventRegistrationRequest;
import com.openbook.openbook.basicuser.dto.EventLayoutCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaCreateData;
import com.openbook.openbook.basicuser.dto.response.EventBasicData;
import com.openbook.openbook.basicuser.dto.response.EventDetail;
import com.openbook.openbook.basicuser.dto.response.EventLayoutStatus;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.event.dto.EventDTO;
import com.openbook.openbook.event.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.event.service.EventTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.AlarmType;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.AlarmService;
import com.openbook.openbook.user.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final UserService userService;
    private final EventService eventService;
    private final EventTagService eventTagService;
    private final UserEventLayoutService userEventLayoutService;
    private final BoothService boothService;
    private final AlarmService alarmService;
    private final S3Service s3Service;

    @Transactional
    public void eventRegistration(final Long userId, final EventRegistrationRequest request) {
        User user = userService.getUserOrException(userId);

        dateValidityCheck(request.openDate(), request.closeDate());
        dateValidityCheck(request.boothRecruitmentStartDate(), request.boothRecruitmentEndDate());
        dateValidityCheck(request.boothRecruitmentEndDate(),request.openDate());
        if(request.tags().size() != request.tags().stream().distinct().count()){
            throw new OpenBookException(ErrorCode.ALREADY_TAG_DATA);
        }
        List<LayoutAreaCreateData> areaData = getLayoutAreaList(request.areaClassifications(), request.areaMaxNumbers());
        EventLayoutCreateData layoutData = new EventLayoutCreateData(request.layoutType(),request.layoutImages(), areaData);
        EventLayout layout = userEventLayoutService.createEventLayout(layoutData);

        EventDTO eventDto = EventDTO.builder()
                .manager(user)
                .name(request.name())
                .location(request.location())
                .description(request.description())
                .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .layout(layout)
                .openDate(request.openDate())
                .closeDate(request.closeDate())
                .b_RecruitmentStartDate(request.boothRecruitmentStartDate())
                .b_RecruitmentEndDate(request.boothRecruitmentEndDate())
                .build();
        Event event = eventService.createEvent(eventDto);
        for(String tag : request.tags()) {
            eventTagService.createEventTag(tag, event);
        }
        alarmService.createAlarm(user, userService.getAdminOrException(), AlarmType.EVENT_REQUEST, eventDto.name());
    }

    @Transactional(readOnly = true)
    public Slice<EventBasicData> getEventBasicData(Pageable pageable, String eventProgress) {
        Slice<Event> events = eventService.getEventsWithProgress(pageable, eventProgress);
        return events.map(
                event -> EventBasicData.of(
                        event, eventTagService.getEventTags(event.getId())
                                .stream()
                                .map(EventTag::getName)
                                .toList())
        );
    }

    @Transactional(readOnly = true)
    public EventDetail getEventDetail(final Long userId, final Long eventId) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        List<String> tags = eventTagService.getEventTags(event.getId()).stream().map(EventTag::getName).toList();
        int boothCount = boothService.getBoothCountByEvent(event);
        return EventDetail.of(event, tags, boothCount, Objects.equals(event.getManager().getId(), userId));
    }

    @Transactional(readOnly = true)
    public EventLayoutStatus getEventLayoutStatus(Long eventId) {
        Event event = eventService.getEventOrException(eventId);
        if(isNotRecruitmentPeriod(event.getBoothRecruitmentStartDate(), event.getBoothRecruitmentEndDate())) {
            throw new OpenBookException(ErrorCode.INACCESSIBLE_PERIOD);
        }
        return userEventLayoutService.getLayoutStatus(event.getLayout());
    }

    private void dateValidityCheck(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new OpenBookException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    private boolean isNotRecruitmentPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        return now.isBefore(startDate) || now.isAfter(endDate);
    }

    private List<LayoutAreaCreateData> getLayoutAreaList(List<String> classifications, List<Integer> maxNumbers) {
        if(classifications.size()!=maxNumbers.size()) {
            throw new OpenBookException(ErrorCode.INVALID_LAYOUT_ENTRY);
        }
        return IntStream.range(0, classifications.size())
                .mapToObj( i -> new LayoutAreaCreateData(classifications.get(i), maxNumbers.get(i)))
                .toList();
    }

}
