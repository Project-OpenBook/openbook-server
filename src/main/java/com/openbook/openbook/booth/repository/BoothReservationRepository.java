package com.openbook.openbook.booth.repository;

import com.openbook.openbook.booth.entity.BoothReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothReservationRepository extends JpaRepository<BoothReservation, Long> {
}
