package com.openbook.openbook.service.booth;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import com.openbook.openbook.domain.booth.dto.BoothReservationStatus;
import com.openbook.openbook.repository.booth.BoothReservationDetailRepository;
import com.openbook.openbook.exception.ErrorCode;
import com.openbook.openbook.exception.OpenBookException;
import com.openbook.openbook.domain.user.User;
import com.openbook.openbook.service.booth.dto.BoothReservationDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<BoothReservationDetailDto> getReservationDetails(Long reserveId) {
        List<BoothReservationDetail> details = boothReservationDetailRepository
                .findByLinkedReservationId(reserveId);

        return details.stream()
                .map(BoothReservationDetailDto::of)
                .collect(Collectors.toList());
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

    @Transactional
    public void modifyReservationDetail(BoothReservation reservation, List<String> addTimes, List<Long> deleteTimes){
        if(addTimes != null){
            checkAvailableTime(addTimes, reservation.getLinkedBooth());
            for(String time : addTimes){
                if(boothReservationDetailRepository.existsByLinkedReservationAndTime(reservation, time)){
                    throw new OpenBookException(ErrorCode.ALREADY_RESERVED_TIME);
                }
            }
            createReservationDetail(addTimes, reservation, reservation.getLinkedBooth());
        }

        if(deleteTimes != null){
            for(Long deleteId : deleteTimes){
                BoothReservationDetail detail = getReservationDetailOrException(deleteId);
                if(!detail.getStatus().equals(BoothReservationStatus.EMPTY)){
                    throw new OpenBookException(ErrorCode.RESERVATION_EXIST);
                }
                boothReservationDetailRepository.delete(detail);
            }
        }
    }

}
