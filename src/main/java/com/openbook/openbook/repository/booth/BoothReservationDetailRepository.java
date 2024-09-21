package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BoothReservationDetailRepository extends JpaRepository<BoothReservationDetail, Long> {
    List<BoothReservationDetail> findBoothReservationDetailsByLinkedReservationId(Long reservationId);
    boolean existsByTime(String date);

    boolean existsByLinkedReservation_DateAndTime(LocalDate date, String time);

    List<BoothReservationDetail> findBoothReservationDetailsByTime(String time);
}
