package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothReservation;
import com.openbook.openbook.booth.entity.BoothReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothReservationDetailRepository extends JpaRepository<BoothReservationDetail, Long> {
    List<BoothReservationDetail> findBoothReservationDetailsByLinkedReservation(BoothReservation boothReservation);
}
