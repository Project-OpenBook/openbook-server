package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothReservationDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.repository.BoothReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BoothReservationService {
    private final BoothReservationRepository boothReservationRepository;
    public BoothReservation createBoothReservation(BoothReservationDTO boothReservationDTO, Booth booth){
        return boothReservationRepository.save(
                BoothReservation.builder()
                        .content(boothReservationDTO.content())
                        .date(boothReservationDTO.date())
                        .linkedBooth(booth)
                        .build()
        );
    }

    public boolean hasExistDate(LocalDate date, Booth booth){
        return boothReservationRepository.existsByDateAndLinkedBooth(date, booth);
    }

    public BoothReservation getReservationByBootAndDate(LocalDate date, Booth booth){
        return boothReservationRepository.findBoothReservationByDateAndLinkedBooth(date, booth);
    }
}
