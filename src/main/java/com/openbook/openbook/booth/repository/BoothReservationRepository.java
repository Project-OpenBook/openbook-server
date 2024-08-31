package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.Booth;
import com.openbook.openbook.booth.entity.BoothReservation;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoothReservationRepository extends JpaRepository<BoothReservation, Long> {
    boolean existsByDateAndLinkedBooth(LocalDate date, Booth booth);
    BoothReservation findBoothReservationByDateAndLinkedBooth(LocalDate date, Booth booth);
    List<BoothReservation> findBoothReservationByLinkedBooth(Booth booth);
}
