package com.openbook.openbook.booth.service;

import com.openbook.openbook.booth.controller.request.ReserveRegistrationRequest;
import com.openbook.openbook.booth.controller.response.BoothReservationDetailResponse;
import com.openbook.openbook.booth.controller.response.BoothReservationsResponse;
import com.openbook.openbook.booth.controller.response.BoothReserveDetailManageResponse;
import com.openbook.openbook.booth.controller.response.BoothReserveManageResponse;
import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.dto.BoothReservationDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.repository.BoothReservationRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.global.util.S3Service;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final BoothService boothService;
    private final BoothReservationDetailService boothReservationDetailService;
    private final BoothReservationRepository boothReservationRepository;
    private final S3Service s3Service;

    public List<BoothReservationsResponse> getAllBoothReservations(long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        List<BoothReservation> boothReservations = getBoothReservations(booth.getId());
        List<BoothReservationsResponse> boothReservationsResponses = new ArrayList<>();
        for(BoothReservation reservation : boothReservations){
            List<BoothReservationDetailResponse> details = boothReservationDetailService.getReservationDetailsByReservation(reservation.getId())
                    .stream().map(BoothReservationDetailResponse::of).toList();
            boothReservationsResponses.add(BoothReservationsResponse.of(reservation, details));
        }

        return boothReservationsResponses;
    }

    public void reserveBooth(Long userId, Long detailId){
        User user = userService.getUserOrException(userId);
        BoothReservationDetail boothReservationDetail = boothReservationDetailService.getBoothReservationDetailOrException(detailId);
        checkValidReservationDetail(boothReservationDetail);
        boothReservationDetailService.setUserToReservation(user, boothReservationDetail);
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
        BoothReservation boothReservation = createBoothReservation(
                new BoothReservationDTO(request.name(), request.description(), request.date(),
                        request.image(), request.price()), booth);
        boothReservationDetailService.createReservationDetail(request.times(), boothReservation);
    }

    @Transactional
    public List<BoothReserveManageResponse> getAllManageReservations(Long userId, Long boothId){
        Booth booth = getValidBoothOrException(userId, boothId);

        List<BoothReservation> boothReservations = getBoothReservations(booth.getId());
        List<BoothReserveManageResponse> boothReserveManageResponses = new ArrayList<>();

        for(BoothReservation reservation : boothReservations){
            List<BoothReserveDetailManageResponse> detailManages =
                    boothReservationDetailService.getReservationDetailsByReservation(reservation.getId())
                            .stream().map(BoothReserveDetailManageResponse::of).toList();
            boothReserveManageResponses.add(BoothReserveManageResponse.of(reservation, detailManages));
        }
        return boothReserveManageResponses;
    }

    public BoothReservation createBoothReservation(BoothReservationDTO boothReservationDTO, Booth booth){
        return boothReservationRepository.save(
                BoothReservation.builder()
                        .name(boothReservationDTO.name())
                        .description(boothReservationDTO.description())
                        .date(boothReservationDTO.date())
                        .imageUrl(s3Service.uploadFileAndGetUrl(boothReservationDTO.image()))
                        .linkedBooth(booth)
                        .build()
        );
    }

    public boolean hasExistDate(LocalDate date, Booth booth){
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

    private Booth getValidBoothOrException(Long userId, Long boothId) {
        User user = userService.getUserOrException(userId);
        Booth booth = boothService.getBoothOrException(boothId);
        if (user != booth.getManager()) {
            throw new OpenBookException(ErrorCode.FORBIDDEN_ACCESS);
        }
        if (!booth.getStatus().equals(BoothStatus.APPROVE)) {
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        return booth;
    }
}
