package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BoothReservationRepository extends JpaRepository<BoothReservation, Long> {
    boolean existsBoothReservationByDateAndLinkedBooth(LocalDate date, Booth booth);
    BoothReservation findBoothReservationByDateAndLinkedBooth(LocalDate date, Booth booth);
}
