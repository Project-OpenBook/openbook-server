package com.openbook.openbook.basicuser.service;

import static com.openbook.openbook.global.util.Formatter.getDateTime;

import com.openbook.openbook.basicuser.dto.request.BoothRegistrationRequest;
import com.openbook.openbook.basicuser.dto.response.BoothBasicData;
import com.openbook.openbook.basicuser.dto.response.BoothDetail;
import com.openbook.openbook.booth.dto.BoothDTO;
import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.service.BoothService;
import com.openbook.openbook.event.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.service.EventService;
import com.openbook.openbook.event.service.LayoutAreaService;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoothService {

    private final BoothService boothService;
    private final EventService eventService;
    private final LayoutAreaService layoutAreaService;
    private final UserService userService;
    private final S3Service s3Service;

    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = userService.getUserOrException(userId);
        Event event = eventService.getEventOrException(request.linkedEvent());
        
        LocalDateTime open = getDateTime(event.getOpenDate() + request.openTime());
        LocalDateTime close = getDateTime(event.getCloseDate() + request.closeTime());

        dateTimePeriodCheck(open, close, event);

        if(hasReservationData(request.layoutAreas())){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "이미 예약된 자리 입니다.");
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
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "승인 처리 된 부스가 아닙니다.");
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
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "시간 입력 오류");
        }
        LocalDate now = LocalDate.now();
        if(now.isBefore(event.getBoothRecruitmentStartDate()) || now.isAfter(event.getBoothRecruitmentEndDate())){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "모집 기간이 아닙니다.");
        }
    }

}
