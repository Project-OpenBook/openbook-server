package com.openbook.openbook.booth.service.common;

import com.openbook.openbook.booth.controller.response.BoothReservationDetailResponse;
import com.openbook.openbook.booth.controller.response.BoothReservationsResponse;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.core.BoothReservationDetailService;
import com.openbook.openbook.booth.service.core.BoothReservationService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonReservationService {
    private final BoothService boothService;
    private final BoothReservationService boothReservationService;
    private final BoothReservationDetailService boothReservationDetailService;
    private final UserService userService;

    public List<BoothReservationsResponse> getAllBoothReservations(long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        List<BoothReservation> boothReservations = boothReservationService.getBoothReservations(booth.getId());
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
}
