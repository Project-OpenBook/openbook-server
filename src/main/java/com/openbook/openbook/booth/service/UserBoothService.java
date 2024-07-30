package com.openbook.openbook.booth.service;

import static com.openbook.openbook.global.util.Formatter.getDateTime;

import com.openbook.openbook.booth.controller.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothBasicData;
import com.openbook.openbook.booth.controller.response.BoothDetail;
import com.openbook.openbook.booth.dto.BoothDTO;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.dto.BoothTagDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.dto.BoothAreaStatus;
import com.openbook.openbook.booth.service.core.BoothAreaService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.booth.service.core.BoothTagService;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.booth.entity.BoothArea;
import com.openbook.openbook.event.service.core.EventService;
import com.openbook.openbook.booth.controller.response.BoothAreaData;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.dto.AlarmType;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.AlarmService;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoothService {

    private final BoothService boothService;
    private final BoothTagService boothTagService;
    private final EventService eventService;
    private final BoothAreaService boothAreaService;
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
        if(hasReservationData(request.requestAreas())){
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
        boothAreaService.setBoothToArea(request.requestAreas(), booth);

        if(request.boothTag().size() != request.boothTag().stream().distinct().count()){
            throw new OpenBookException(ErrorCode.ALREADY_TAG_DATA);
        }

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
                booth -> BoothBasicData.of(
                        booth, booth.getLinkedEvent(), boothTagService.getBoothTag(booth.getId()))
        );
    }

    @Transactional(readOnly = true)
    public BoothDetail getBoothDetail(Long userId, Long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        List<BoothAreaData> boothAreaData = boothAreaService.getBoothAreasByBoothId(boothId)
                .stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());
        return BoothDetail.of(booth, boothAreaData, boothTagService.getBoothTag(booth.getId()), Objects.equals(booth.getManager().getId(), userId));
    }

    @Transactional(readOnly = true)
    public Slice<BoothBasicData> searchByBoothName(Pageable pageable, String boothName){
        return boothService.getBoothByName(pageable, boothName, BoothStatus.APPROVE).map(
                booth -> BoothBasicData.of(
                        booth, booth.getLinkedEvent(), boothTagService.getBoothTag(booth.getId()))
        );
    }

    @Transactional(readOnly = true)
    public Slice<BoothBasicData> searchByBoothTag(Pageable pageable, String boothTag){
        return boothTagService.getBoothByTag(pageable, boothTag).map(
                booth -> BoothBasicData.of(
                        booth, booth.getLinkedEvent(), boothTagService.getBoothTag(booth.getId()))
        );
    }

    private boolean hasReservationData(List<Long> eventLayoutAreaList){
        for(Long id : eventLayoutAreaList){
            BoothArea boothArea = boothAreaService.getBoothAreaOrException(id);
            if(!boothArea.getStatus().equals(BoothAreaStatus.EMPTY)){
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
