package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.repository.BoothReservationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothReservationDetailService {
    private final BoothReservationDetailRepository boothReservationDetailRepository;
    public void createReservationDetail(List<String> reservationDetails, BoothReservation boothReservation){
        for(String time : reservationDetails){
            boothReservationDetailRepository.save(
                    BoothReservationDetail.builder()
                            .boothReservation(boothReservation)
                            .time(time)
                            .build()
            );
        }
    }

    public List<BoothReservationDetail> getReservationDetailsByReservation(Long reservationId){
        return boothReservationDetailRepository.findBoothReservationDetailsByLinkedReservationId(reservationId);
    }
}
