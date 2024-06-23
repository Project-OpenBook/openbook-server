package com.openbook.openbook.basicuser.service;

import com.openbook.openbook.basicuser.dto.request.BoothRegistrationRequest;
import com.openbook.openbook.basicuser.dto.response.BoothBasicData;
import com.openbook.openbook.basicuser.dto.response.BoothDetail;
import com.openbook.openbook.booth.dto.BoothStatus;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.EventLayoutArea;
import com.openbook.openbook.event.repository.EventLayoutAreaRepository;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.eventmanager.dto.BoothAreaData;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.repository.UserRepository;
import com.openbook.openbook.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserBoothService {

    private final BoothRepository boothRepository;
    private final UserEventLayoutAreaService userEventLayoutAreaService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventLayoutAreaRepository eventLayoutAreaRepository;
    private final S3Service s3Service;
    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = getUserOrException(userId);
        Event event = getEventOrException(request.linkedEvent());
        
        LocalDateTime open = timeFormat(request.openTime(), event.getOpenDate());
        LocalDateTime close = timeFormat(request.closeTime(), event.getCloseDate());

        dateTimePeriodCheck(open, close, event);

        if(userEventLayoutAreaService.hasReservationData(request.layoutAreas())){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "이미 예약된 자리 입니다.");
        }else{
            Booth booth = Booth.builder()
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

            boothRepository.save(booth);
            userEventLayoutAreaService.requestBoothLocation(request.layoutAreas(), booth);
        }
    }

    @Transactional(readOnly = true)
    public Slice<BoothBasicData> getBoothBasicData(Pageable pageable) {
        Slice<Booth> booths = boothRepository.findAllByStatus(BoothStatus.APPROVE, pageable);

        return booths.map(booth -> {
            return BoothBasicData.of(booth, booth.getLinkedEvent());
        });
    }

    @Transactional(readOnly = true)
    public BoothDetail getBoothDetail(Long boothId){
        Booth booth = getBoothOrException(boothId);
        List<EventLayoutArea> eventLayoutAreas = eventLayoutAreaRepository.findAllByLinkedBoothId(boothId);
        List<BoothAreaData> boothAreaData = eventLayoutAreas.stream()
                .map(BoothAreaData::of)
                .collect(Collectors.toList());

        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "승인 처리 된 부스가 아닙니다.");
        }
        return BoothDetail.of(booth, boothAreaData);

    }

    public int getBoothCountByLinkedEvent(Event event) {
        return boothRepository.countByLinkedEvent(event);
    }

    private void dateTimePeriodCheck(LocalDateTime open, LocalDateTime close, Event event){
        LocalTime openTime = open.toLocalTime();
        LocalTime closeTime = close.toLocalTime();
        if(openTime.isAfter(closeTime)){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "시간 입력 오류");
        }

        LocalDate now = LocalDate.now();

        if(now.isBefore(event.getBoothRecruitmentStartDate()) || now.isAfter(event.getBoothRecruitmentEndDate())){
            throw new OpenBookException(HttpStatus.BAD_REQUEST, "모집 기간이 아닙니다.");
        }
    }

    private LocalDateTime timeFormat(String time, LocalDate date){
        String dateTime = date.toString() + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    private Event getEventOrException(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "행사 정보를 찾을 수 없습니다."));
    }

    private User getUserOrException(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."));
    }

    private Booth getBoothOrException(Long boothId){
        return boothRepository.findById(boothId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "부스 정보를 찾을 수 없습니다."));
    }
}
