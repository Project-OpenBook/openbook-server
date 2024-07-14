package com.openbook.openbook.basicuser.service;

import static com.openbook.openbook.global.util.Formatter.getDateTime;

import com.openbook.openbook.basicuser.dto.request.BoothRegistrationRequest;
import com.openbook.openbook.basicuser.dto.response.BoothBasicData;
import com.openbook.openbook.basicuser.dto.response.BoothDetail;
import com.openbook.openbook.booth.dto.BoothDTO;
import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.dto.BoothTagDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothTag;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.booth.service.BoothTagService;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.event.service.LayoutAreaService;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.dto.AlarmType;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.AlarmService;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoothService {

    private final BoothService boothService;
    private final BoothTagService boothTagService;
    private final EventService eventService;
    private final LayoutAreaService layoutAreaService;
    private final UserService userService;
    private final AlarmService alarmService;
    private final S3Service s3Service;

    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = userService.getUserOrException(userId);
        Event event = eventService.getEventOrException(request.linkedEvent());
        LocalDateTime open = getDateTime(event.getOpenDate() + " " + request.openTime());
        LocalDateTime close = getDateTime(event.getCloseDate() + " " + request.closeTime());
        dateTimePeriodCheck(open, close, event);
        if(hasReservationData(request.layoutAreas())){
            throw new OpenBookException(ErrorCode.ALREADY_RESERVED_AREA);
        }
        BoothDTO boothDTO = BoothDTO.builder()
                .linkedEvent(event)
                .manager(user)
                .name(request.name())
                .description(request.description())
                .mainImageUrl(s3Service.uploadFileAndGetUrl(request.mainImage()))
                .accountBankName(request.accountBankName())
                .accountNumber(request.accountNumber())
                .openTime(open)
                .closeTime(close)
                .build();

        Booth booth = boothService.createBooth(boothDTO);
        layoutAreaService.setBoothLocation(request.layoutAreas(), booth);

        for(String boothTag : request.boothTag()){
            BoothTagDTO boothTagDTO = BoothTagDTO.builder()
                    .content(boothTag)
                    .booth(booth)
                    .build();

            boothTagService.createBoothTag(boothTagDTO);
        }

        alarmService.createAlarm(user, event.getManager(), AlarmType.BOOTH_REQUEST, booth.getName());
    }


    @Transactional(readOnly = true)
    public Slice<BoothBasicData> getBoothBasicData(Pageable pageable) {
        return boothService.getBoothsByStatus(pageable, BoothStatus.APPROVE).map(
                booth -> BoothBasicData.of(booth, booth.getLinkedEvent())
        );
    }

    @Transactional(readOnly = true)
    public BoothDetail getBoothDetail(Long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        List<BoothAreaData> boothAreaData = layoutAreaService.getLayoutAreasByBoothId(boothId)
                .stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());
        return BoothDetail.of(booth, boothAreaData);
    }

    private boolean hasReservationData(List<Long> eventLayoutAreaList){
        for(Long id : eventLayoutAreaList){
            EventLayoutArea eventLayoutArea = layoutAreaService.getAreaOrException(id);
            if(!eventLayoutArea.getStatus().equals(EventLayoutAreaStatus.EMPTY)){
                return true;
            }
        }
        return false;
    }

    private void dateTimePeriodCheck(LocalDateTime open, LocalDateTime close, Event event){
        if(open.isAfter(close)){
            throw new OpenBookException(ErrorCode.INVALID_DATE_RANGE);
        }
        LocalDate now = LocalDate.now();
        if(now.isBefore(event.getBoothRecruitmentStartDate()) || now.isAfter(event.getBoothRecruitmentEndDate())){
            throw new OpenBookException(ErrorCode.INACCESSIBLE_PERIOD);
        }
    }

}
