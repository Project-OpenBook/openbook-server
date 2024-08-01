package com.openbook.openbook.event.service;


import com.openbook.openbook.event.controller.request.EventRegistrationRequest;
import com.openbook.openbook.event.dto.EventLayoutCreateData;
import com.openbook.openbook.booth.dto.BoothAreaCreateData;
import com.openbook.openbook.event.controller.response.UserEventData;
import com.openbook.openbook.event.controller.response.EventDetail;
import com.openbook.openbook.event.controller.response.EventLayoutStatus;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.event.dto.EventDTO;
import com.openbook.openbook.event.entity.dto.EventStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.entity.EventTag;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.event.service.core.EventTagService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.TagUtil;
import com.openbook.openbook.user.entity.dto.AlarmType;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.AlarmService;
import com.openbook.openbook.user.service.core.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

        List<BoothAreaCreateData> areaData = getBoothAreaCreateList(request.areaClassifications(), request.areaMaxNumbers());
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

        if (request.tags() != null) {
            TagUtil.getValidTagsOrException(request.tags()).forEach(
                    tag ->  eventTagService.createEventTag(tag, event)
            );
        }

        alarmService.createAlarm(user, userService.getAdminOrException(), AlarmType.EVENT_REQUEST, eventDto.name());
    }

    public Slice<UserEventData> getEventsSearchBy(Pageable pageable, String searchType, String name) {
        Slice<Event> events  = switch (searchType) {
            case "eventName" -> eventService.getEventsWithNameMatchBy(name, EventStatus.APPROVE, pageable);
            case "tagName" -> eventTagService.getEventsWithTagNameMatchBy(name, EventStatus.APPROVE, pageable);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
        if(events.isEmpty()) {
            throw new OpenBookException(ErrorCode.SEARCH_RESULTS_NOT_FOUND);
        }
        return events.map(
                event -> UserEventData.of(event, eventTagService.getEventTags(event.getId()))
        );
    }

    @Transactional(readOnly = true)
    public Slice<UserEventData> getEventBasicData(Pageable pageable, String eventProgress) {
        Slice<Event> events = eventService.getEventsWithProgress(pageable, eventProgress);
        return events.map(
                event -> UserEventData.of(event, eventTagService.getEventTags(event.getId()))
        );
    }

    @Transactional(readOnly = true)
    public EventDetail getEventDetail(final Long userId, final Long eventId) {
        Event event = eventService.getEventOrException(eventId);
        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        List<EventTag> tags = eventTagService.getEventTags(event.getId());
        int boothCount = boothService.getBoothCountByEvent(event);
        return EventDetail.of(event, userId, tags, boothCount);
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

    private List<BoothAreaCreateData> getBoothAreaCreateList(List<String> classifications, List<Integer> maxNumbers) {
        if(classifications.size()!=maxNumbers.size()) {
            throw new OpenBookException(ErrorCode.INVALID_LAYOUT_ENTRY);
        }
        return IntStream.range(0, classifications.size())
                .mapToObj( i -> new BoothAreaCreateData(classifications.get(i), maxNumbers.get(i)))
                .toList();
    }

}
