package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.BoothReservationDetail;
import com.openbook.openbook.booth.entity.dto.BoothReservationStatus;
import com.openbook.openbook.booth.repository.BoothReservationDetailRepository;
import com.openbook.openbook.global.exception.ErrorCode;
import com.openbook.openbook.global.exception.OpenBookException;
import com.openbook.openbook.user.entity.User;
import com.openbook.openbook.user.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothReservationDetailService {
    private final BoothReservationDetailRepository boothReservationDetailRepository;

    public BoothReservationDetail getBoothReservationDetailOrException(final Long id) {
        return boothReservationDetailRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }
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

    @Transactional
    public void setUserToReservation(User user, BoothReservationDetail boothReservationDetail){
        boothReservationDetail.updateUser(BoothReservationStatus.WAITING, user);
    }


}
