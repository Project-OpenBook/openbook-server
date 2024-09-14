package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothReservationDetailRepository extends JpaRepository<BoothReservationDetail, Long> {
    List<BoothReservationDetail> findBoothReservationDetailsByLinkedReservationId(Long reservationId);
}
