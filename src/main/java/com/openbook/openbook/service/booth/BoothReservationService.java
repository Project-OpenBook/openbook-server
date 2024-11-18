package com.openbook.openbook.service.booth;

import com.openbook.openbook.api.booth.request.ReserveModifyRequest;
import com.openbook.openbook.api.booth.request.ReserveRegistrationRequest;
import com.openbook.openbook.api.booth.request.ReserveStatusUpdateRequest;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.domain.booth.dto.BoothStatus;
import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.user.dto.AlarmType;
import com.openbook.openbook.repository.booth.BoothReservationRepository;
import com.openbook.openbook.service.booth.dto.BoothReservationDateDto;
import com.openbook.openbook.service.booth.dto.BoothReservationDetailDto;
import com.openbook.openbook.service.booth.dto.BoothReservationDto;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.service.booth.dto.BoothReservationUpdateData;
import com.openbook.openbook.service.user.AlarmService;
import com.openbook.openbook.util.S3Service;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.user.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return getGroupReservation(boothId, false);
    }

    @Transactional
    public List<BoothReservationDto> getAllManageReservations(Long userId, Long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getManager().getId().equals(userId)){
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        return getGroupReservation(boothId, true);
    }

    private List<BoothReservationDto> getGroupReservation(Long boothId, boolean isAdmin) {
        List<BoothReservation> reservations = getBoothReservations(boothId);
        Map<String, List<BoothReservation>> groupedByName = reservations.stream()
                .collect(Collectors.groupingBy(BoothReservation::getName));

        return groupedByName.values().stream()
                .map(groupedReservations -> {
                    BoothReservation firstReservation = groupedReservations.get(0);
                    List<BoothReservationDateDto> reservationsByDate = groupedReservations.stream()
                            .collect(Collectors.groupingBy(BoothReservation::getDate))
                            .entrySet().stream()
                            .map(dateEntry -> {
                                LocalDate date = dateEntry.getKey();
                                List<BoothReservationDetailDto> details = dateEntry.getValue().stream()
                                        .flatMap(reservation -> reservationDetailService
                                                .getReservationDetails(reservation.getId()).stream())
                                        .map(detail -> {
                                            if (isAdmin) {
                                                return detail;
                                            } else {
                                                return new BoothReservationDetailDto(
                                                        detail.id(),
                                                        detail.times(),
                                                        detail.status(),
                                                        null
                                                );
                                            }
                                        })
                                        .collect(Collectors.toList());

                                return BoothReservationDateDto.of(date, details);
                            })
                            .collect(Collectors.toList());

                    return BoothReservationDto.of(firstReservation, reservationsByDate);
                })
                .collect(Collectors.toList());
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
        if(boothReservationDetail.getLinkedReservation().getDate().isBefore(LocalDate.now())){
            throw new OpenBookException(ErrorCode.UNAVAILABLE_RESERVED_DATE);
        }
    }

    @Transactional
    public void addReservation(Long userId, ReserveRegistrationRequest request, Long boothId) {
        Booth booth = getValidBoothOrException(userId, boothId);
        checkAvailableDate(request.dates(), booth);
        checkDuplicateDates(request, booth);

        for(LocalDate date : request.dates()){
            BoothReservation reservation = boothReservationRepository.save(
                    BoothReservation.builder()
                            .name(request.name())
                            .description(request.description())
                            .price(request.price())
                            .date(date)
                            .imageUrl(s3Service.uploadFileAndGetUrl(request.image()))
                            .linkedBooth(booth)
                            .build()
            );
            reservationDetailService.createReservationDetail(request.times(), reservation, booth);
        }
    }

    public List<BoothReservation> getBoothReservations(Long boothId){
        return boothReservationRepository.findBoothReservationByLinkedBoothId(boothId);
    }

    private void checkAvailableDate(List<LocalDate> dates, Booth booth){
        for(LocalDate date : dates){
            if(date.isBefore(booth.getLinkedEvent().getOpenDate())
                    || date.isAfter(booth.getLinkedEvent().getCloseDate())){
                throw new OpenBookException(ErrorCode.INVALID_RESERVED_DATE);
            }
        }

    }

    private void checkDuplicateDates(ReserveRegistrationRequest request, Booth booth){
        for(LocalDate date : request.dates()){
            if(boothReservationRepository.existsByLinkedBoothIdAndDateAndName(booth.getId(), date, request.name())){
                throw new OpenBookException(ErrorCode.ALREADY_RESERVED_DATE);
            }
        }

    }

    @Transactional
    public void changeReserveStatus(Long detailId, ReserveStatusUpdateRequest request, Long userId){
        BoothReservationDetail boothReservationDetail =
                reservationDetailService.getReservationDetailOrException(detailId);
        VerifyUserIsManagerOfBooth(boothReservationDetail.getLinkedReservation().getLinkedBooth(), userId);

        BoothReservationStatus status = BoothReservationStatus.fromString(request.status());

        if(boothReservationDetail.getStatus().equals(status)){
            throw new OpenBookException(ErrorCode.ALREADY_PROCESSED);
        }

        User manager = userService.getUserOrException(userId);
        User reserveUser = boothReservationDetail.getUser();
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

    @Transactional
    public void modifyReservation(Long userId, long reserveId, ReserveModifyRequest request){
        BoothReservation reservation = getBoothReservationOrException(reserveId);
        VerifyUserIsManagerOfBooth(reservation.getLinkedBooth(), userId);

        reservation.updateReservation(BoothReservationUpdateData.builder()
                        .name(request.name())
                        .description(request.description())
                        .image((request.image()!=null) ? s3Service.uploadFileAndGetUrl(request.image()):null)
                        .price(request.price())
                        .date(request.date())
                        .build()
        );

        if(request.timesToAdd() != null || request.timesToDelete() != null){
            reservationDetailService.modifyReservationDetail(reservation, request.timesToAdd(), request.timesToDelete());
        }
    }

    private BoothReservation getBoothReservationOrException(long reserveId){
        return boothReservationRepository.findById(reserveId).orElseThrow(
                () -> new OpenBookException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }
}
