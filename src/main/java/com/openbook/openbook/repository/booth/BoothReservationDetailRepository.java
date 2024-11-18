package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BoothReservationDetailRepository extends JpaRepository<BoothReservationDetail, Long> {
    boolean existsByLinkedReservationAndTime(BoothReservation boothReservation, String time);
    List<BoothReservationDetail> findByLinkedReservationId(Long reservationId);
}
