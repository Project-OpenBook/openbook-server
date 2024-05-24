package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.dto.request.BoothRegistrationRequest;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.repository.BoothRepository;
import com.openbook.openbook.event.entity.Event;
import com.openbook.openbook.event.entity.dto.EventLayoutAreaStatus;
import com.openbook.openbook.event.repository.EventRepository;
import com.openbook.openbook.global.S3Service;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.UserRepository;
import com.openbook.openbook.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothService {

    private final BoothRepository boothRepository;
    private final  BoothLocationService boothLocationService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    @Transactional
    public void boothRegistration(Long userId, BoothRegistrationRequest request){
        User user = getUserOrException(userId);
        Event event = getEventOrException(request.linkedEvent());
        
        LocalDateTime open = timeFormat(request.openTime(), event.getOpenDate());
        LocalDateTime close = timeFormat(request.closeTime(), event.getCloseDate());

        dateTimePeriodCheck(open, close, event);

        boothLocationService.boothLocationApplication(request.locations());

        Booth booth = Booth.builder()
                .linkedEvent(event)
                .manager(user)
                .name(request.name())
                .description(request.description())
                .mainImageUrl(uploadAndGetS3ImageUrl(request.mainImage()))
                .accountNumber(request.accountNumber())
                .openTime(open)
                .closeTime(close)
                .build();

        boothRepository.save(booth);

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


    private String uploadAndGetS3ImageUrl(MultipartFile image) {
        String imageName = getRandomFileName(image);
        s3Service.uploadFileToS3(image, imageName);
        return s3Service.getFileUrlFromS3(imageName);
    }

    private String getRandomFileName(MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }

    private Event getEventOrException(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "행사 정보를 찾을 수 없습니다."));
    }

    private User getUserOrException(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new OpenBookException(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."));
    }
}
