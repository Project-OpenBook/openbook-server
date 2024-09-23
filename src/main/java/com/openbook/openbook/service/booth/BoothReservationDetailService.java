package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.repository.booth.BoothReservationDetailRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothReservationDetailService {

    private final BoothReservationDetailRepository boothReservationDetailRepository;

    public BoothReservationDetail getReservationDetailOrException(final Long id) {
        return boothReservationDetailRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    public boolean hasExistTime(List<String> times){
        for(String time : times){
            if(!boothReservationDetailRepository.existsByTime(time)){
                return false;
            }
        }
        return true;
    }

    public void createReservationDetail(List<String> reservationDetails, BoothReservation reservation){
        for(String time : reservationDetails){
            boothReservationDetailRepository.save(
                    BoothReservationDetail.builder()
                            .boothReservation(reservation)
                            .time(time)
                            .build()
            );
        }
    }

    @Transactional
    public void setUserToReservation(User user, BoothReservationDetail boothReservationDetail){
        boothReservationDetail.updateUser(BoothReservationStatus.WAITING, user);
    }


}
