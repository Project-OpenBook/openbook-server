package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.Booth;
import com.openbook.openbook.domain.booth.BoothReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoothReservationRepository extends JpaRepository<BoothReservation, Long> {
    List<BoothReservation> findBoothReservationByLinkedBoothId(Long boothId);
    boolean existsByLinkedBoothIdAndDateAndName(Long boothId, LocalDate date, String name);
}
