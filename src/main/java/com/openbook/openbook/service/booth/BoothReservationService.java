package com.openbook.openbook.service.booth;

import com.openbook.openbook.api.booth.request.ReserveRegistrationRequest;
import com.openbook.openbook.api.booth.request.ReserveStatusUpdateRequest;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.user.dto.AlarmType;
import com.openbook.openbook.repository.booth.BoothReservationRepository;
import com.openbook.openbook.service.booth.dto.BoothReservationDto;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.user.AlarmService;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.user.UserService;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothReservationService {
    private final UserService userService;
    private final S3Service s3Service;

    private final BoothService boothService;
    private final BoothReservationDetailService reservationDetailService;
    private final BoothReservationRepository boothReservationRepository;
    private final AlarmService alarmService;


    private Booth getValidBoothOrException(long userId, long boothId) {
        Booth booth = boothService.getBoothOrException(boothId);
        if (booth.getManager().getId()!=userId) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!booth.getStatus().equals(BoothStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        return booth;
    }

    public List<BoothReservationDto> getReservationsByBooth(long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        return getBoothReservations(booth.getId()).stream().map(BoothReservationDto::of).toList();
    }

    @Transactional
    public List<BoothReservationDto> getAllManageReservations(Long userId, Long boothId){
        Booth booth = getValidBoothOrException(userId, boothId);
        return getBoothReservations(booth.getId()).stream().map(BoothReservationDto::of).toList();
    }

    public void reserveBooth(Long userId, Long detailId){
        User user = userService.getUserOrException(userId);
        BoothReservationDetail boothReservationDetail = reservationDetailService.
                getReservationDetailOrException(detailId);
        checkValidReservationDetail(boothReservationDetail);
        reservationDetailService.setUserToReservation(user, boothReservationDetail);
    }

    private void checkValidReservationDetail(BoothReservationDetail boothReservationDetail){
        if(!boothReservationDetail.getStatus().equals(BoothReservationStatus.EMPTY)) {
            throw new OpenBookException(ErrorCode.ALREADY_RESERVED_SERVICE);
        }

        if(LocalTime.parse(boothReservationDetail.getTime()).isBefore(LocalTime.now())){
            throw new OpenBookException(ErrorCode.UNAVAILABLE_RESERVED_TIME);
        }
    }

    @Transactional
    public void addReservation(Long userId, ReserveRegistrationRequest request, Long boothId) {
        Booth booth = getValidBoothOrException(userId, boothId);
        if (hasExistDate(request.date(), booth)) {
            throw new OpenBookException(ErrorCode.ALREADY_RESERVED_DATE);
        }
        checkAvailableTime(request, booth);
        checkDuplicateTimes(request.times());
        BoothReservation reservation = boothReservationRepository.save(
                BoothReservation.builder()
                        .name(request.name())
                        .description(request.description())
                        .date(request.date())
                        .imageUrl(s3Service.uploadFileAndGetUrl(request.image()))
                        .linkedBooth(booth)
                        .build()
        );
        reservationDetailService.createReservationDetail(request.times(), reservation);
    }

    private boolean hasExistDate(LocalDate date, Booth booth){
        return boothReservationRepository.existsByDateAndLinkedBooth(date, booth);
    }

    public List<BoothReservation> getBoothReservations(Long boothId){
        return boothReservationRepository.findBoothReservationByLinkedBoothId(boothId);
    }

    private void checkAvailableTime(ReserveRegistrationRequest request, Booth booth){
        for(String time : request.times()){
            if(booth.getOpenTime().toLocalTime().isAfter(LocalTime.parse(time))
                    || booth.getCloseTime().toLocalTime().isBefore(LocalTime.parse(time))){
                throw new OpenBookException(ErrorCode.UNAVAILABLE_RESERVED_TIME);
            }
        }
    }

    private void checkDuplicateTimes(List<String> times) {
        Set<String> validTimes = new HashSet<>();
        times.stream()
                .map(String::trim)
                .forEach(time -> {
                    if (!validTimes.add(time)) {
                        throw new OpenBookException(ErrorCode.DUPLICATE_RESERVED_TIME);
                    }
                });
    }

    @Transactional
    public void changeReserveStatus(Long detailId, ReserveStatusUpdateRequest request, Long userId){
        BoothReservationDetail boothReservationDetail =
                reservationDetailService.getReservationDetailOrException(detailId);
        VerifyUserIsManagerOfBooth(boothReservationDetail.getLinkedReservation().getLinkedBooth(), userId);
        User manager = userService.getUserOrException(userId);
        User reserveUser = boothReservationDetail.getUser();
        BoothReservationStatus status = BoothReservationStatus.fromString(request.status());

        if(boothReservationDetail.getStatus().equals(status)){
            throw new OpenBookException(ErrorCode.ALREADY_PROCESSED);
        }

        if(status.equals(BoothReservationStatus.COMPLETE)){
            boothReservationDetail.updateUser(status, reserveUser);
            alarmService.createAlarm(manager, reserveUser,
                    AlarmType.RESERVE_APPROVED, boothReservationDetail.getLinkedReservation().getName());
        } else if (status.equals(BoothReservationStatus.EMPTY)) {
            boothReservationDetail.updateUser(status, null);
            alarmService.createAlarm(manager, reserveUser,
                    AlarmType.RESERVE_REJECTED, boothReservationDetail.getLinkedReservation().getName());
        }
    }

    private void VerifyUserIsManagerOfBooth(Booth booth, long userId){
        if(!booth.getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
    }
}
