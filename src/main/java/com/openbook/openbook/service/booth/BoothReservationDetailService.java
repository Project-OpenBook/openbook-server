package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.Booth;
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

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoothReservationDetailService {

    private final BoothReservationDetailRepository boothReservationDetailRepository;

    public BoothReservationDetail getReservationDetailOrException(final Long id) {
        return boothReservationDetailRepository.findById(id).orElseThrow(() ->
                new OpenBookException(ErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    public void createReservationDetail(List<String> times, BoothReservation reservation, Booth booth){
        checkAvailableTime(times, booth);

        for(String time : times){
            boothReservationDetailRepository.save(
                    BoothReservationDetail.builder()
                            .boothReservation(reservation)
                            .time(time)
                            .build()
            );
        }
    }

    private void checkAvailableTime(List<String> times, Booth booth){
        Set<String> validTimes = new HashSet<>();
        times.stream()
                .map(String::trim)
                .forEach(time -> {
                    if(booth.getOpenTime().toLocalTime().isAfter(LocalTime.parse(time))
                            || booth.getCloseTime().toLocalTime().isBefore(LocalTime.parse(time))){
                        throw new OpenBookException(ErrorCode.UNAVAILABLE_RESERVED_TIME);
                    }
                    if(!validTimes.add(time)){
                        throw new OpenBookException(ErrorCode.DUPLICATE_RESERVED_TIME);
                    }
                });
    }

    @Transactional
    public void setUserToReservation(User user, BoothReservationDetail boothReservationDetail){
        boothReservationDetail.updateUser(BoothReservationStatus.WAITING, user);
    }


}
