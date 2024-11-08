package com.openbook.openbook.service.event;

import com.openbook.openbook.service.booth.dto.BoothAreaCreateData;
import com.openbook.openbook.api.event.request.EventRegistrationRequest;
import com.openbook.openbook.service.event.dto.EventDto;
import com.openbook.openbook.service.event.dto.EventLayoutCreateData;
import com.openbook.openbook.service.event.dto.EventLayoutDto;
import com.openbook.openbook.domain.event.EventLayout;
import com.openbook.openbook.domain.event.dto.EventStatus;
import com.openbook.openbook.domain.event.Event;
import com.openbook.openbook.repository.event.EventRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.domain.user.dto.AlarmType;
import com.openbook.openbook.service.user.AlarmService;
import com.openbook.openbook.service.user.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final EventTagService eventTagService;
    private final EventLayoutService eventLayoutService;

    private final AlarmService alarmService;
    private final S3Service s3Service;

    private final EventRepository eventRepository;

    public Event getEventOrException(final Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new OpenBookException(ErrorCode.EVENT_NOT_FOUND)
        );
    }

    public int findBoothCount(final Long eventId) {
        return getEventOrException(eventId).getEventBooths().size();
    }



    @Transactional
    public void createEvent(final Long userId, final EventRegistrationRequest request) {
        User user = userService.getUserOrException(userId);
        dateValidityCheck(request.openDate(), request.closeDate());
        dateValidityCheck(request.boothRecruitmentStartDate(), request.boothRecruitmentEndDate());
        dateValidityCheck(request.boothRecruitmentEndDate(),request.openDate());
        List<BoothAreaCreateData> areaData = getBoothAreaCreateList(request.areaClassifications(), request.areaMaxNumbers());
        EventLayoutCreateData layoutData = new EventLayoutCreateData(request.layoutType(),request.layoutImages(), areaData);
        EventLayout layout = eventLayoutService.createEventLayout(layoutData);
        Event event = eventRepository.save(Event.builder()
                .manager(user)
                .layout(layout)
                .location(request.location())
                .name(request.name())
                .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .description(request.description())
                .openDate(request.openDate())
                .closeDate(request.closeDate())
                .boothRecruitmentStartDate(request.boothRecruitmentStartDate())
                .boothRecruitmentEndDate(request.boothRecruitmentEndDate())
                .build()
        );
        if (request.tags() != null) {
            eventTagService.createEventTags(request.tags(), event);
        }
        alarmService.createAlarm(user, userService.getAdminOrException(), AlarmType.EVENT_REQUEST, event.getName());
    }

    public Slice<EventDto> getEventsSearchBy(String searchType, String name, int page, String sort) {
        PageRequest pageRequest = createEventPageRequest(page, sort, searchType);
        Slice<Event> events  = switch (searchType) {
            case "eventName" -> eventRepository.findAllByNameAndStatus(pageRequest, name, EventStatus.APPROVE);
            case "tagName" -> eventTagService.getEventsWithTagNameMatchBy(name, EventStatus.APPROVE, pageRequest);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
        return events.map(EventDto::of);
    }



    @Transactional(readOnly = true)
    public EventDto getEventById(final Long eventId) {
        Event event = getEventOrException(eventId);
        if(!event.getStatus().equals(EventStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return EventDto.of(event);
    }

    @Transactional(readOnly = true)
    public Slice<EventDto> getEventsByProgress(Pageable pageable, String eventProgress) {
        Slice<Event> events = getEventsWithProgress(pageable, eventProgress);
        return events.map(EventDto::of);
    }

    private Slice<Event> getEventsWithProgress(Pageable pageable, String progress) {
        return switch (progress) {
            case "all" -> eventRepository.findAllByStatus(pageable, EventStatus.APPROVE);
            case "ongoing" -> eventRepository.findAllOngoing(pageable);
            case "recruiting" -> eventRepository.findAllRecruiting(pageable);
            case "terminated" -> eventRepository.findAllTerminated(pageable);
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }

    @Transactional(readOnly = true)
    public Page<EventDto> getEventsByStatus(Pageable pageable, String status) {
        Page<Event> events = (status.equals("all"))
                ? eventRepository.findAll(pageable)
                : eventRepository.findAllByStatus(pageable, getEventStatus(status));
        return events.map(EventDto::of);
    }


    @Transactional(readOnly = true)
    public Slice<EventDto> getEventsByManager(Long managerId, Pageable pageable, String status) {
        userService.getUserOrException(managerId);
        Slice<Event> events = (status.equals("ALL"))
                ? eventRepository.findAllByManagerId(pageable, managerId)
                : eventRepository.findAllByManagerIdAndStatus(pageable, managerId, EventStatus.valueOf(status));
        return events.map(EventDto::of);
    }



    @Transactional
    public void changeEventStatus(Long eventId, EventStatus status) {
        Event event = getEventOrException(eventId);
        event.updateStatus(status);
        AlarmType t = (status==EventStatus.APPROVE) ? AlarmType.EVENT_APPROVED : AlarmType.EVENT_REJECTED;
        alarmService.createAlarm(userService.getAdminOrException(), event.getManager(), t, event.getName());
    }

    private EventStatus getEventStatus(String status) {
        return switch (status) {
            case "waiting" -> EventStatus.WAITING;
            case "approved" -> EventStatus.APPROVE;
            case "rejected" -> EventStatus.REJECT;
            default -> throw new OpenBookException(ErrorCode.INVALID_PARAMETER);
        };
    }
    @Transactional(readOnly = true)
    public EventLayoutDto getEventLayoutStatus(Long eventId) {
        Event event = getEventOrException(eventId);
        if(isNotRecruitmentPeriod(event.getBoothRecruitmentStartDate(), event.getBoothRecruitmentEndDate())) {
            throw new OpenBookException(ErrorCode.INACCESSIBLE_PERIOD);
        }
        return EventLayoutDto.of(event.getLayout());
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

    private PageRequest createEventPageRequest(int page, String sort, String searchType){
        Sort.Direction direction = "asc".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = searchType.equals("eventName") ? "registeredAt" : "linkedEvent.registeredAt";

        return PageRequest.of(page, 8, Sort.by(direction, sortProperty));
    }


}
