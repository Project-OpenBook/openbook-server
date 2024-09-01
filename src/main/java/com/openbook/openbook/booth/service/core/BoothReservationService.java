package com.openbook.openbook.booth.service.core;

import com.openbook.openbook.booth.dto.BoothReservationDTO;
import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.repository.BoothReservationRepository;
import com.openbook.openbook.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoothReservationService {
    private final BoothReservationRepository boothReservationRepository;
    private final S3Service s3Service;
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
}
