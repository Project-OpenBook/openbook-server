package com.openbook.openbook.booth.service.common;

import com.openbook.openbook.booth.controller.response.BoothReservationDetailResponse;
import com.openbook.openbook.booth.controller.response.BoothReservationsResponse;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.dto.BoothStatus;
import com.openbook.openbook.booth.service.core.BoothReservationDetailService;
import com.openbook.openbook.booth.service.core.BoothReservationService;
import com.openbook.openbook.booth.service.core.BoothService;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonReservationService {
    private final BoothService boothService;
    private final BoothReservationService boothReservationService;
    private final BoothReservationDetailService boothReservationDetailService;

    public List<BoothReservationsResponse> getAllBoothReservations(long boothId){
        Booth booth = boothService.getBoothOrException(boothId);
        if(!booth.getStatus().equals(BoothStatus.APPROVE)){
            throw new OpenBookException(ErrorCode.BOOTH_NOT_APPROVED);
        }
        List<BoothReservation> boothReservations = boothReservationService.getBoothReservations(booth.getId());
        List<BoothReservationsResponse> boothReservationsResponses = new ArrayList<>();
        for(BoothReservation boothReservation : boothReservations){
            List<BoothReservationDetailResponse> details = boothReservationDetailService.getReservationDetailsByReservation(boothReservation)
                    .stream().map(BoothReservationDetailResponse::of).toList();
            boothReservationsResponses.add(BoothReservationsResponse.of(boothReservation, details));
        }

        return boothReservationsResponses;
    }
}
