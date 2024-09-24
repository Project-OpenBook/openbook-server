package com.openbook.openbook.repository.booth;

import com.openbook.openbook.domain.booth.BoothReservation;
import com.openbook.openbook.domain.booth.BoothReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoothReservationDetailRepository extends JpaRepository<BoothReservationDetail, Long> {

    BoothReservationDetail findByLinkedReservation(BoothReservation boothReservation);
}
