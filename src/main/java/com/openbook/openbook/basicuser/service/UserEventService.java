package com.openbook.openbook.basicuser.service;


import com.openbook.openbook.basicuser.dto.request.EventRegistrationRequest;
import com.openbook.openbook.basicuser.dto.EventLayoutCreateData;
import com.openbook.openbook.basicuser.dto.LayoutAreaCreateData;
import com.openbook.openbook.basicuser.dto.response.EventBasicData;
import com.openbook.openbook.basicuser.dto.response.EventDetail;
import com.openbook.openbook.basicuser.dto.response.EventLayoutStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayout;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.repository.UserRepository;
import com.openbook.openbook.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserEventLayoutService userEventLayoutService;
    private final UserBoothService userBoothService;
    private final S3Service s3Service;

    @Transactional
    public void eventRegistration(final Long userId, final EventRegistrationRequest request) {
        User user = getUserOrException(userId);

        dateValidityCheck(request.openDate(), request.closeDate());
        dateValidityCheck(request.boothRecruitmentStartDate(), request.boothRecruitmentEndDate());
        dateValidityCheck(request.boothRecruitmentEndDate(),request.openDate());

        List<LayoutAreaCreateData> areaData = getLayoutAreaList(request.areaClassifications(), request.areaMaxNumbers());
        EventLayoutCreateData layoutData = new EventLayoutCreateData(request.layoutType(),request.layoutImages(), areaData);
        EventLayout layout = userEventLayoutService.createEventLayout(layoutData);

        Event event = Event.builder()
                .manager(user)
                .name(request.name())
                .location(request.location())
                .description(request.description())
                .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .layout(layout)
                .openDate(request.openDate())
                .closeDate(request.closeDate())
                .boothRecruitmentStartDate(request.boothRecruitmentStartDate())
                .boothRecruitmentEndDate(request.boothRecruitmentEndDate())
                .build();
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Slice<EventBasicData> getEventBasicData(Pageable pageable, String eventProgress) {
        Slice<Event> events = switch (eventProgress) {
            case "all" -> eventRepository.findAllApproved(pageable);
            case "ongoing" -> eventRepository.findAllOngoing(pageable);
            case "recruiting" -> eventRepository.findAllRecruiting(pageable);
            case "terminated" -> eventRepository.findAllTerminated(pageable);
            default -> throw new OpenBookException(HttpStatus.BAD_REQUEST, "요청 값이 잘못되었습니다.");
        };
        return events.map(EventBasicData::of);
    }

    @Transactional(readOnly = true)
    public EventDetail getEventDetail(final Long userId, final Long eventId) {
        Event event = getEventOrException(eventId);
        int boothCount = userBoothService.getBoothCountByLinkedEvent(event);
        return EventDetail.of(event, boothCount, Objects.equals(event.getManager().getId(), userId));
    }

    @Transactional(readOnly = true)
    public EventLayoutStatus getEventLayoutStatus(Long eventId) {
        Event event = getEventOrException(eventId);
        if(isNotRecruitmentPeriod(event.getBoothRecruitmentStartDate(), event.getBoothRecruitmentEndDate())) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "확인 가능한 기간이 아닙니다.");
        }
        return userEventLayoutService.getLayoutStatus(event.getLayout());
    }

    private void dateValidityCheck(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "날짜 입력 오류");
        }
    }

    private boolean isNotRecruitmentPeriod(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        return now.isBefore(startDate) || now.isAfter(endDate);
    }

    private List<LayoutAreaCreateData> getLayoutAreaList(List<String> classifications, List<Integer> maxNumbers) {
        if(classifications.size()!=maxNumbers.size()) {
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "배치도 구역 입력 오류");
        }
        return IntStream.range(0, classifications.size())
                .mapToObj( i -> new LayoutAreaCreateData(classifications.get(i), maxNumbers.get(i)))
                .toList();
    }

    private User getUserOrException(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );
    }

    private Event getEventOrException(Long id) {
        return eventRepository.findById(id).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "행사가 존재하지 않습니다.")
        );
    }

}
